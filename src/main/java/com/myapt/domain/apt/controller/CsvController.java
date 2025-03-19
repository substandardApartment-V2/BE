package com.myapt.domain.apt.controller;

import com.myapt.domain.apt.service.CsvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apt")
public class CsvController {
    private final CsvService csvService;
    private static final Logger log = LoggerFactory.getLogger(CsvController.class);
    // CsvService를 생성자 주입 방식으로 주입받습니다.
    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @Value("${apt.api.base-url}")
    private String baseUrl;

    @Value("${apt.api.endpoint}")
    private String endpoint;

    @Value("${apt.api.service-key}")
    private String serviceKey;

    @Value("${apt.api2.base-url}")
    private String kblandBaseUrl;

    // 월별 건설 예정 아파트 수 저장
    @PostMapping("/uploadV1")
    public ResponseEntity<ApiResponse> saveMonthlyConstructionData() {
        try {
            String url = baseUrl + endpoint + "?serviceKey=" + serviceKey + "&returnType=JSON";
            log.info("건설 예정 아파트 데이터 저장 시작 - URL: {}", url);
            csvService.processPlannedAptsData(url, false);
            log.info("월별 건설 예정 아파트 데이터 저장 성공");
            return ResponseEntity.ok(new ApiResponse(true, "월별 건설예정 아파트 데이터가 성공적으로 저장되었습니다."));
        } catch (RuntimeException e) {
            log.error("건설 예정 아파트 데이터 저장 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "월별 건설예정 아파트 데이터 저장 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    // 월별 전국 아파트 평균 가격 수 저장
    @PostMapping("uploadV2")
    public ResponseEntity<ApiResponse> saveMonthlyAvgPriceData() {
        try {
            csvService.processAveragePriceData(kblandBaseUrl, false);
            log.info("월별 전국 아파트 평균 가격 데이터 저장 성공");
            return ResponseEntity.ok(new ApiResponse(true, "데이터 저장 성공"));
        } catch (RuntimeException e) {
            log.error("데이터 저장 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "데이터 저장 실패: " + e.getMessage()));
        }
    }

    public class ApiResponse {
        private boolean success;
        private String message;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
