package com.myapt.domain.apt.service;

import com.opencsv.CSVReader;
import com.myapt.domain.apt.entity.AvgPrices;
import com.myapt.domain.apt.repository.CsvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvServiceImpl implements CsvService {
    private final CsvRepository csvRepository;

    @Override
    public void saveCsvData(MultipartFile file) {
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
                    // 예: "2024년 3월"에서 '월'에 해당하는 숫자만 파싱
                    String monthStr = fields[1].replaceAll("[^0-9]", ""); // 숫자만 남김
                    Long month = Long.parseLong(monthStr.substring(4)); // '월'에 해당하는 부분만 추출
                    avgPrices.setMonth(month);

                    // CSV의 2번째 행이 AvgPrices의 avgPrice와 매칭
                    // 예: "450,577"에서 쉼표와 따옴표를 제거 후 숫자에 000을 붙임
                    try {
                        // 쉼표와 따옴표 제거
                        String avgPriceStr = fields[2].replace(",", "").replace("\"", "").trim();

                        // 문자열이 비어있지 않은지 확인
                        if (avgPriceStr.isEmpty()) {
                            throw new NumberFormatException("숫자 변환을 위한 문자열이 비어 있습니다.");
                        }

                        // 숫자로 변환 가능 여부 확인
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

        csvRepository.saveAll(avgPricesList);
    }
}
