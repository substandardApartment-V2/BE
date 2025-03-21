package com.myapt.domain.apt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapt.domain.apt.entity.AvgPrices;
import com.myapt.domain.apt.entity.PlannedApts;
import com.myapt.domain.apt.repository.AvgPricesRepository;
import com.myapt.domain.apt.repository.PlannedAptsRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CsvServiceImpl implements CsvService {
    private static final Logger log = LoggerFactory.getLogger(CsvServiceImpl.class);
    private final AvgPricesRepository avgPricesRepository;
    private final PlannedAptsRepository plannedAptsRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${apt.api.base-url}")
    private String baseUrl;

    @Value("${apt.api.endpoint}")
    private String endpoint;

    @Value("${apt.api.service-key}")
    private String serviceKey;

    @Value("${apt.api2.base-url}")
    private String kblandBaseUrl;

    // 건설 예정 아파트 데이터 처리
    @Transactional
    public void processPlannedAptsData(String url, boolean isScheduled) {
        log.info("건설 예정 아파트 데이터 처리 시작 - URL: {}, 스케줄링 여부: {}", url, isScheduled);
        try {
            Map<String, Long> dateCountMap = fetchPlannedAptsData(url);
            String dataHash = String.valueOf(dateCountMap.hashCode());
            log.debug("데이터 해시값 생성: {}", dataHash);

            // 스케줄링 모드에서 해시값 비교
            if (isScheduled) {
                Optional<PlannedApts> latest = plannedAptsRepository.findTopByOrderByYearDescMonthDesc();
                if (latest.isPresent() && latest.get().getDataHash().equals(dataHash)) {
                    log.info("스케줄링 모드: 데이터 변경 없음 - 업데이트 생략");
                    return;
                }
                log.info("스케줄링 모드: 데이터 변경 감지 - 업데이트 진행");
            }

            savePlannedAptsToDatabase(dateCountMap, dataHash);
            log.info("DB 저장 완료 - 저장된 데이터 수: {}", dateCountMap.size());
            if (isScheduled) log.info("스케줄링 작업: 건설 예정 아파트 데이터 업데이트 성공");
        } catch (Exception e) {
            if (isScheduled) {
                log.error("스케줄링 모드: 데이터 처리 실패 - {}", e.getMessage(), e);
            } else {
                throw new RuntimeException("건설 예정 아파트 데이터 처리 실패: " + e.getMessage(), e);
            }
        }
    }

    // 스케줄링: 매일 09:00에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void scheduledProcessPlannedAptsData() {
        log.info("스케줄링 작업 시작 - 매일 자정 실행 (건설 예정 아파트)");
        String url = baseUrl + endpoint + "?serviceKey=" + serviceKey + "&returnType=JSON";
        processPlannedAptsData(url, true);
    }

    // 외부 API에서 건설 예정 아파트 데이터 가져오기
    private Map<String, Long> fetchPlannedAptsData(String baseUrl) throws Exception {
        Map<String, Long> dateCountMap = new HashMap<>();
        int page = 1;
        int perPage = 100;
        int totalCount;

        do {
            String url = baseUrl + "&page=" + page + "&perPage=" + perPage;
            log.info("페이지 {} 데이터 가져오기 - URL: {}", page, url);
            try (InputStream inputStream = downloadFromUrl(url)) {
                JsonNode root = objectMapper.readTree(inputStream);
                log.info("페이지 {}에서 받은 원본 JSON 데이터: {}", page, root.toString());
                totalCount = root.path("totalCount").asInt(0);
                log.info("총 데이터 수 (totalCount): {}", totalCount);
                JsonNode dataArray = root.path("data");
                log.info("추출된 data 배열: {}", dataArray.toString());

                if (!dataArray.isArray() || dataArray.size() == 0) {
                    log.warn("페이지 {}에서 데이터 없음", page);
                    break;
                }

                for (JsonNode node : dataArray) {
                    log.debug("개별 데이터 노드: {}", node.toString());
                    String scheduledMonth = node.path("연월").asText(null);
                    log.debug("추출된 연월: {}", scheduledMonth);
                    if (scheduledMonth == null || scheduledMonth.isEmpty()) {
                        log.warn("잘못된 연월 데이터 스킵: {}", node.toString());
                        continue;
                    }

                    String[] yearMonth = scheduledMonth.split("-");
                    if (yearMonth.length != 2) {
                        log.warn("잘못된 연월 형식 스킵: {}", scheduledMonth);
                        continue;
                    }

                    String key = scheduledMonth; // "YYYY-MM"
                    dateCountMap.merge(key, 1L, Long::sum);
                    log.debug("연월 {}에 대한 카운트 업데이트: {}", key, dateCountMap.get(key));
                }
            }
            page++;
            log.info("페이지 {} 처리 완료 - 현재 dateCountMap: {}", page - 1, dateCountMap);
        } while ((page - 1) * perPage < totalCount);

        log.info("최종 파싱된 데이터: {}", dateCountMap);
        return dateCountMap;
    }

    // URL에서 데이터를 다운로드하는 헬퍼 메서드
    private InputStream downloadFromUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        int responseCode = connection.getResponseCode();
        log.info("API 응답 코드: {}", responseCode);
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("API call failed: HTTP " + responseCode);
        }
        return connection.getInputStream();
    }

    // 건설 예정 아파트 데이터를 데이터베이스에 저장
    @Transactional
    private void savePlannedAptsToDatabase(Map<String, Long> dateCountMap, String dataHash) {
        List<PlannedApts> plannedAptsList = new ArrayList<>();
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
            plannedApts.setDataHash(dataHash); // 해시값 저장
            plannedAptsList.add(plannedApts);
        }
        plannedAptsRepository.saveAll(plannedAptsList);
    }

    // 전국 아파트 평균 매매가 다운로드 및 DB 저장
    @Override
    @Transactional
    public void processAveragePriceData(String url, boolean isScheduled) {
        log.info("평균 가격 데이터 처리 시작 - URL: {}, 스케줄링 여부: {}", url, isScheduled);

        // 주어진 URL에서 아파트 평균 가격 데이터를 가져옴
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // API 호출이 성공(HTTP 200)인지 확인
        if (response.getStatusCode() != HttpStatus.OK) {
            if (isScheduled) {
                // 스케줄링 작업 중 실패 시 로그만 남기고 종료
                log.error("API 호출 실패 (스케줄링 모드) - HTTP 상태 코드: {}", response.getStatusCode());
                return;
            }
            // 수동 호출 시 예외를 발생시켜 호출자에게 문제 알림
            log.error("API 호출 실패 (수동 모드) - HTTP 상태 코드: {}", response.getStatusCode());
            throw new RuntimeException("API 호출 실패: HTTP " + response.getStatusCode());
        }
        log.info("API 호출 성공 - 응답 수신 완료");

        try {
            // JSON 파싱
            log.debug("JSON 데이터 파싱 시작");
            JsonNode root = objectMapper.readTree(response.getBody());
            List<String> dateList = objectMapper.convertValue(
                    root.path("dataBody").path("data").path("날짜리스트"),
                    new TypeReference<List<String>>() {}
            );
            String dateListHash = String.valueOf(dateList.hashCode());
            log.debug("날짜 리스트 해시값 생성 - 해시: {}", dateListHash);

            // 스케줄링 시 변경 여부 확인
            if (isScheduled) {
                Optional<AvgPrices> latest = avgPricesRepository.findTopByOrderByYearDescMonthDesc();
                if (latest.isPresent() && latest.get().getDateListHash().equals(dateListHash)) {
                    log.info("스케줄링 모드: 데이터 변경 없음 - 업데이트 생략");
                    return;
                }
                log.info("스케줄링 모드: 데이터 변경 감지 - 업데이트 진행");
            }

            List<AvgPrices> avgPricesList = new ArrayList<>();
            JsonNode dataListNode = root.path("dataBody").path("data").path("데이터리스트");
            log.debug("지역 데이터 순회 시작");

            for (JsonNode regionNode : dataListNode) {
                if ("전국".equals(regionNode.path("지역명").asText())) {
                    log.debug("전국 데이터 발견 - 가격 리스트 추출 시작");
                    List<Double> prices = objectMapper.convertValue(
                            regionNode.path("dataList"),
                            new TypeReference<List<Double>>() {}
                    );
                    for (int i = 0; i < dateList.size() && i < prices.size(); i++) {
                        String date = dateList.get(i);
                        Long year = Long.parseLong(date.substring(0, 4));
                        Long month = Long.parseLong(date.substring(4, 6));
                        Long avgPrice = Math.round(prices.get(i));

                        AvgPrices avgPrices = avgPricesRepository.findByYearAndMonth(year, month)
                                .orElse(new AvgPrices());
                        avgPrices.setYear(year);
                        avgPrices.setMonth(month);
                        avgPrices.setAvgPrice(avgPrice);
                        avgPrices.setDateListHash(dateListHash);
                        avgPricesList.add(avgPrices);
                    }
                    log.debug("전국 데이터 처리 완료 - 저장할 데이터 수: {}", avgPricesList.size());
                    break;
                }
            }

            avgPricesRepository.saveAll(avgPricesList);
            log.info("DB 저장 완료 - 저장된 데이터 수: {}", avgPricesList.size());
            if (isScheduled) log.info("스케줄링 작업: 데이터 업데이트 성공");
        } catch (JsonProcessingException e) {
            if (isScheduled) {
                log.error("스케줄링 모드: JSON 파싱 실패 - 오류: {}", e.getMessage());
            } else {
                log.error("수동 모드: JSON 파싱 실패 - 오류: {}", e.getMessage());
                throw new RuntimeException("JSON 파싱 실패: " + e.getMessage(), e);
            }
        }
    }

    // 스케줄링: 매일 09:00에 실행(UTC 기준 00:00은 KST 기준 09:00이다.)
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void scheduledProcessAveragePriceData() {
        log.info("스케줄링 작업 시작 - 매일 자정 실행");
        processAveragePriceData(kblandBaseUrl, true);
    }
}
