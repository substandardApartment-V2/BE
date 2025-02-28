package com.myapt.domain.apt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapt.domain.apt.entity.AvgPrices;
import com.myapt.domain.apt.entity.PlannedApts;
import com.myapt.domain.apt.repository.AvgPricesRepository;
import com.myapt.domain.apt.repository.PlannedAptsRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Value("${apt.api.base-url}")
    private String baseUrl;

    @Value("${apt.api.endpoint}")
    private String endpoint;

    @Value("${apt.api.service-key}")
    private String serviceKey;

    @Override
    public void saveCsvDataV1(MultipartFile file) {
        List<AvgPrices> avgPricesList = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] fields;
            int lineCount = 0;

            while ((fields = csvReader.readNext()) != null) {
                lineCount++;
                if (lineCount < 4) {
                    continue;
                }
                if (fields.length < 3) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CSV 파일의 형식이 잘못되었습니다.");
                }

                try {
                    AvgPrices avgPrices = new AvgPrices();
                    avgPrices.setId(fields[0]);
                    String monthStr = fields[1].replaceAll("[^0-9]", "");
                    Long month = Long.parseLong(monthStr.substring(4));
                    avgPrices.setMonth(month);

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

    @Override
    public void downloadAndSaveCsvDataV2(String baseUrl) {
        try {
            Map<String, Long> dateCountMap = fetchAllData(baseUrl);
            saveDataToDatabase(dateCountMap);
        } catch (Exception e) {
            throw new RuntimeException("월별 건설예정 아파트 데이터 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void scheduledDownloadAndSaveCsvData() {
        try {
            String url = baseUrl + endpoint +
                    "?serviceKey=" + serviceKey +
                    "&returnType=JSON";
            Map<String, Long> dateCountMap = fetchAllData(url);
            saveDataToDatabase(dateCountMap);
        } catch (Exception e) {
            // 예외 처리만 남김
        }
    }

    private Map<String, Long> fetchAllData(String baseUrl) throws Exception {
        Map<String, Long> dateCountMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        int page = 1;
        int perPage = 100;
        int totalCount = 0;

        do {
            String url = baseUrl + "&page=" + page + "&perPage=" + perPage;

            try (InputStream inputStream = downloadFileFromUrl(url)) {
                JsonNode root = mapper.readTree(inputStream);
                totalCount = root.path("totalCount").asInt();
                JsonNode dataArray = root.path("data");

                if (!dataArray.isArray()) {
                    break;
                }

                for (JsonNode node : dataArray) {
                    String scheduledMonth = node.path("연월").asText(null);
                    if (scheduledMonth == null || scheduledMonth.isEmpty()) {
                        continue;
                    }

                    try {
                        String[] yearMonth = scheduledMonth.split("-");
                        if (yearMonth.length != 2) {
                            continue;
                        }
                        String key = scheduledMonth;
                        dateCountMap.put(key, dateCountMap.getOrDefault(key, 0L) + 1);
                    } catch (Exception e) {
                        continue;
                    }
                }
            }

            page++;
        } while ((page - 1) * perPage < totalCount);

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
                existing.setCount(count);
                plannedAptsRepository.save(existing);
                for (int i = 1; i < existingRecords.size(); i++) {
                    plannedAptsRepository.delete(existingRecords.get(i));
                }
            } else {
                PlannedApts plannedApts = new PlannedApts();
                plannedApts.setYear(year);
                plannedApts.setMonth(month);
                plannedApts.setCount(count);
                plannedAptsRepository.save(plannedApts);
            }
        }
    }
}