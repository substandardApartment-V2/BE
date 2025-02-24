package com.myapt.domain.apt.service;

import com.myapt.domain.apt.entity.AvgPrices;
import com.myapt.domain.apt.entity.PlannedApts;
import com.myapt.domain.apt.repository.AvgPricesRepository;
import com.myapt.domain.apt.repository.PlannedAptsRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CsvServiceImpl implements CsvService {
    private final AvgPricesRepository avgPricesRepository;
    private final PlannedAptsRepository plannedAptsRepository;

    // 전국 '월별 평균매매가' CSV 파일 전처리 후, 저장
    @Override
    public void saveCsvDataV1(MultipartFile file) {
        List<AvgPrices> avgPricesList = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] fields;
            int lineCount = 0;

            while ((fields = csvReader.readNext()) != null) {
                lineCount++;

                // 4번째 행부터 데이터 처리(저장) 시작
                if (lineCount < 4) {
                    continue;
                }

                if (fields.length < 3) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CSV 파일의 형식이 잘못되었습니다.");
                }

                try {
                    AvgPrices avgPrices = new AvgPrices();

                    // CSV의 0번째 행이 AvgPrices의 id와 매칭
                    avgPrices.setId(fields[0]);

                    // CSV의 1번째 행이 AvgPrices의 month와 매칭
                    String monthStr = fields[1].replaceAll("[^0-9]", ""); // 숫자만 남김
                    Long month = Long.parseLong(monthStr.substring(4)); // '월'에 해당하는 부분만 추출
                    avgPrices.setMonth(month);

                    // CSV의 2번째 행이 AvgPrices의 avgPrice와 매칭
                    try {
                        String avgPriceStr = fields[2].replace(",", "").replace("\"", "").trim();

                        if (avgPriceStr.isEmpty()) {
                            throw new NumberFormatException("숫자 변환을 위한 문자열이 비어 있습니다.");
                        }

                        Long avgPrice = Long.parseLong(avgPriceStr + "000");
                        avgPrices.setAvgPrice(avgPrice);
                    } catch (NumberFormatException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 숫자 형식: " + fields[2], e);
                    }

                    avgPricesList.add(avgPrices);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "CSV 데이터 처리 중 오류가 발생했습니다.", e);
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "CSV 파일 처리 중 오류가 발생했습니다.", e);
        }

        avgPricesRepository.saveAll(avgPricesList);
    }

    // 전국 '월별 건설예정 아파트' CSV 파일 전처리 후, 저장
    @Override
    public void saveCsvDataV2(MultipartFile file) {
        Map<String, Long> dateCountMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) {
                    // 첫 번째 줄(헤더)을 건너뜀
                    continue;
                }

                // CSV 파일의 각 줄을 콤마(,)로 분리하여 첫 번째 필드만 사용
                String[] fields = line.split(",");
                if (fields.length < 1) {
                    continue;
                }

                try {
                    // 첫 번째 필드의 데이터를 "yyyy-MM" 형식으로 파싱
                    String dateField = fields[0].trim();
                    YearMonth yearMonth = YearMonth.parse(dateField, formatter);

                    // 월을 두 자리로 형식화하여 일관된 키 생성
                    String key = yearMonth.getYear() + "-" + String.format("%02d", yearMonth.getMonthValue());

                    // 연도와 월을 키로 사용하여 Map에 카운트를 증가시킴
                    dateCountMap.put(key, dateCountMap.getOrDefault(key, 0L) + 1);
                } catch (Exception e) {
                    // 날짜 파싱 실패시 무시
                }
            }

            // 데이터베이스에 저장
            for (Map.Entry<String, Long> entry : dateCountMap.entrySet()) {
                String[] yearMonth = entry.getKey().split("-");
                long year = Long.parseLong(yearMonth[0]);
                long month = Long.parseLong(yearMonth[1]);
                long count = entry.getValue();
                saveToDatabase(year, month, count);
            }

        } catch (Exception e) {
            throw new RuntimeException("CSV 데이터를 처리하는 동안 오류가 발생했습니다.", e);
        }
    }

    private void saveToDatabase(long year, long month, long count) {
        try {
            // 동일한 연도와 월의 모든 레코드를 가져와서 합산
            List<PlannedApts> existingRecords = plannedAptsRepository.findAllByYearAndMonth(year, month);
            if (!existingRecords.isEmpty()) {
                // 기존 count를 모두 합산
                long existingCount = existingRecords.stream().mapToLong(PlannedApts::getCount).sum();
                // 새로운 count와 합산
                long updatedCount = existingCount + count;

                // 기존 레코드를 하나로 통합
                PlannedApts plannedApts = existingRecords.get(0);
                plannedApts.setCount(updatedCount);

                // 나머지 중복 레코드 삭제
                for (int i = 1; i < existingRecords.size(); i++) {
                    plannedAptsRepository.delete(existingRecords.get(i));
                }

                // 통합된 레코드 저장
                plannedAptsRepository.save(plannedApts);
            } else {
                PlannedApts plannedApts = new PlannedApts();
                plannedApts.setYear(year);
                plannedApts.setMonth(month);
                plannedApts.setCount(count);
                plannedAptsRepository.save(plannedApts);
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
