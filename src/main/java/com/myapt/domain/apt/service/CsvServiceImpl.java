package com.myapt.domain.apt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapt.domain.apt.entity.AvgPrices;
import com.myapt.domain.apt.entity.PlannedApts;
import com.myapt.domain.apt.repository.AvgPricesRepository;
import com.myapt.domain.apt.repository.PlannedAptsRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CsvServiceImpl implements CsvService {
    private final AvgPricesRepository avgPricesRepository;
    private final PlannedAptsRepository plannedAptsRepository;

    private static final Logger logger = LoggerFactory.getLogger(CsvServiceImpl.class);

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
    public void downloadAndSaveCsvDataV2(String baseUrl) {
        try {
            Map<String, Long> dateCountMap = fetchAllData(baseUrl); // 전체 데이터 가져오기
            saveDataToDatabase(dateCountMap); // DB에 저장
        } catch (Exception e) {
            logger.error("데이터 처리 중 오류", e);
            throw new RuntimeException("월별 건설예정 아파트 데이터 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private Map<String, Long> fetchAllData(String baseUrl) throws Exception {
        Map<String, Long> dateCountMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        int page = 1;
        int perPage = 100; // 한 번에 100개씩 가져오기 (API 제한 고려)
        int totalCount = 0;

        do {
            String url = baseUrl + "&page=" + page + "&perPage=" + perPage;
            logger.info("API 호출 URL: {}", url);

            try (InputStream inputStream = downloadFileFromUrl(url)) {
                JsonNode root = mapper.readTree(inputStream);
                logger.debug("API 응답: {}", root.toString());

                totalCount = root.path("totalCount").asInt();
                JsonNode dataArray = root.path("data");

                if (!dataArray.isArray()) {
                    logger.warn("데이터 배열이 없음: {}", root.toString());
                    break;
                }

                for (JsonNode node : dataArray) {
                    String scheduledMonth = node.path("연월").asText(null);
                    if (scheduledMonth == null || scheduledMonth.isEmpty()) {
                        logger.debug("연월 값이 유효하지 않음: {}", scheduledMonth);
                        continue;
                    }

                    try {
                        String[] yearMonth = scheduledMonth.split("-");
                        if (yearMonth.length != 2) {
                            logger.warn("연월 형식이 잘못됨: {}", scheduledMonth);
                            continue;
                        }
                        String key = scheduledMonth; // "2026-01" 형식으로 키 사용
                        dateCountMap.put(key, dateCountMap.getOrDefault(key, 0L) + 1);
                    } catch (Exception e) {
                        logger.warn("날짜 파싱 실패: {}, 노드: {}", scheduledMonth, node.toString());
                    }
                }
            }

            page++;
        } while ((page - 1) * perPage < totalCount);

        logger.info("집계된 데이터: {}", dateCountMap);
        return dateCountMap;
    }

    private InputStream downloadFileFromUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("API 호출 실패: HTTP " + responseCode);
        }
        return connection.getInputStream();
    }

    private void saveDataToDatabase(Map<String, Long> dateCountMap) {
        for (Map.Entry<String, Long> entry : dateCountMap.entrySet()) {
            String[] yearMonth = entry.getKey().split("-");
            long year = Long.parseLong(yearMonth[0]);
            long month = Long.parseLong(yearMonth[1]);
            long count = entry.getValue();

            List<PlannedApts> existingRecords = plannedAptsRepository.findAllByYearAndMonth(year, month);
            if (!existingRecords.isEmpty()) {
                PlannedApts existing = existingRecords.get(0);
                existing.setCount(count); // 기존 데이터 갱신
                plannedAptsRepository.save(existing);
                logger.debug("데이터 갱신: year={}, month={}, count={}", year, month, count);
                // 중복 데이터 삭제
                for (int i = 1; i < existingRecords.size(); i++) {
                    plannedAptsRepository.delete(existingRecords.get(i));
                }
            } else {
                PlannedApts plannedApts = new PlannedApts();
                plannedApts.setYear(year);
                plannedApts.setMonth(month);
                plannedApts.setCount(count);
                plannedAptsRepository.save(plannedApts);
                logger.debug("신규 데이터 저장: year={}, month={}, count={}", year, month, count);
            }
        }
        logger.info("DB 저장 완료, 저장된 행 수: {}", dateCountMap.size());
    }
}
