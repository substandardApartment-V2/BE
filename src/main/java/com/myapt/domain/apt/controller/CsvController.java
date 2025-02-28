package com.myapt.domain.apt.controller;

import com.myapt.domain.apt.service.CsvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/apt")
public class CsvController {
    private static final Logger logger = LoggerFactory.getLogger(CsvController.class);
    private final CsvService csvService;

    @Value("${apt.api.base-url}")
    private String baseUrl;

    @Value("${apt.api.endpoint}")
    private String endpoint;

    @Value("${apt.api.service-key}")
    private String serviceKey;

    // CsvService를 생성자 주입 방식으로 주입받습니다.
    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("uploadV1")
    public ResponseEntity<String> uploadCsv_v1(@RequestParam("file") MultipartFile file) {
        try {
            // 파일이 비어있거나 null일 경우 400 오류 반환
            if (file.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일이 비어 있습니다.");
            }

            // 서비스 계층을 통해 CSV 데이터를 저장
            csvService.saveCsvDataV1(file);

            // 성공적으로 저장된 경우 200 응답 반환
            return ResponseEntity.status(HttpStatus.OK).body("CSV 데이터가 성공적으로 저장되었습니다.");

        } catch (ResponseStatusException e) {
            // 잘못된 요청 또는 항목 누락 등 특정 오류에 대한 응답 & ResponseStatusException의 상태 코드와 메시지를 사용하여 응답 생성
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());

        } catch (Exception e) {
            // 일반적인 서버 오류에 대한 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CSV 데이터 저장 중 서버 오류가 발생했습니다.");
        }
    }
    @PostMapping("uploadV2")
    public ResponseEntity<String> uploadCsvV2() {
        try {
            String url = baseUrl + endpoint +
                    "?serviceKey=" + serviceKey +
                    "&returnType=JSON"; // page와 perPage는 서비스에서 동적 처리
            logger.info("기본 API URL: {}", url);
            csvService.downloadAndSaveCsvDataV2(url);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("월별 건설예정 아파트 데이터가 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            logger.error("데이터 저장 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("월별 건설예정 아파트 데이터 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
