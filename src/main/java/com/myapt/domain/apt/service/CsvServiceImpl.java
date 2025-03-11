package com.myapt.domain.apt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapt.domain.apt.entity.PlannedApts;
import com.myapt.domain.apt.repository.AvgPricesRepository;
import com.myapt.domain.apt.repository.PlannedAptsRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    @Scheduled(cron = "0 0 0 1 * ?") // 매달 1일 자정 실행
    public void scheduledDownloadAndSaveCsvData() { //@Scheduled로 주기적으로 실행되며, 외부 API에서 데이터를 가져와 데이터베이스에 저장.
        try {
            String url = baseUrl + endpoint + "?serviceKey=" + serviceKey + "&returnType=JSON";
            Map<String, Long> dateCountMap = fetchAllData(url);
            saveDataToDatabase(dateCountMap);
        } catch (Exception e) {
            System.err.println("Scheduled task failed: " + e.getMessage()); // 로깅 프레임워크 사용 권장
        }
    }

    @Override
    public void downloadAndSaveCsvDataV2(String baseUrl) {
        try {
            Map<String, Long> dateCountMap = fetchAllData(baseUrl);
            saveDataToDatabase(dateCountMap);
        } catch (Exception e) {
            throw new RuntimeException("Failed to download and save planned apts data: " + e.getMessage(), e);
        }
    }

    //외부 API에서 JSON 데이터를 가져와 Map<String, Long>으로 변환.
    private Map<String, Long> fetchAllData(String baseUrl) throws Exception {
        Map<String, Long> dateCountMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        int page = 1;
        int perPage = 100;
        int totalCount;

        do {
            String url = baseUrl + "&page=" + page + "&perPage=" + perPage;
            try (InputStream inputStream = downloadFromUrl(url)) {
                JsonNode root = mapper.readTree(inputStream);
                totalCount = root.path("totalCount").asInt(0);
                JsonNode dataArray = root.path("data");

                if (!dataArray.isArray() || dataArray.size() == 0) {
                    System.out.println("No data found for page " + page);
                    break;
                }

                for (JsonNode node : dataArray) {
                    String scheduledMonth = node.path("연월").asText(null);
                    if (scheduledMonth == null || scheduledMonth.isEmpty()) {
                        System.out.println("Skipping invalid scheduledMonth: " + node.toString());
                        continue;
                    }

                    String[] yearMonth = scheduledMonth.split("-");
                    if (yearMonth.length != 2) {
                        System.out.println("Invalid format for scheduledMonth: " + scheduledMonth);
                        continue;
                    }

                    String key = scheduledMonth; // "YYYY-MM"
                    dateCountMap.merge(key, 1L, Long::sum);
                }
            }
            page++;
        } while ((page - 1) * perPage < totalCount);

        return dateCountMap;
    }

    //URL에서 데이터를 다운로드하는 헬퍼 메서드.
    private InputStream downloadFromUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("API call failed: HTTP " + responseCode);
        }
        return connection.getInputStream();
    }

    //데이터를 데이터베이스에 저장.
    @Transactional
    private void saveDataToDatabase(Map<String, Long> dateCountMap) {
        for (Map.Entry<String, Long> entry : dateCountMap.entrySet()) {
            String[] yearMonth = entry.getKey().split("-");
            long year = Long.parseLong(yearMonth[0]);
            long month = Long.parseLong(yearMonth[1]);
            long count = entry.getValue();

            Optional<PlannedApts> existing = plannedAptsRepository.findByYearAndMonth(year, month);
            PlannedApts plannedApts = existing.orElse(new PlannedApts());
            plannedApts.setYear(year);
            plannedApts.setMonth(month);
            plannedApts.setCount(count);
            plannedAptsRepository.save(plannedApts);
        }
    }
}
