package com.myapt.domain.apt.controller;

import com.myapt.domain.apt.service.CsvService;
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
    private final CsvService csvService;

    @Value("${apt.api.base-url}")
    private String baseUrl;

    @Value("${apt.api.endpoint}")
    private String endpoint;

    @Value("${apt.api.service-key}")
    private String serviceKey;

    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("uploadV1")
    public ResponseEntity<String> uploadCsv_v1(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일이 비어 있습니다.");
            }
            csvService.saveCsvDataV1(file);
            return ResponseEntity.status(HttpStatus.OK).body("CSV 데이터가 성공적으로 저장되었습니다.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CSV 데이터 저장 중 서버 오류가 발생했습니다.");
        }
    }

    @PostMapping("uploadV2")
    public ResponseEntity<String> uploadCsvV2() {
        try {
            String url = baseUrl + endpoint +
                    "?serviceKey=" + serviceKey +
                    "&returnType=JSON";
            csvService.downloadAndSaveCsvDataV2(url);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("월별 건설예정 아파트 데이터가 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("월별 건설예정 아파트 데이터 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}