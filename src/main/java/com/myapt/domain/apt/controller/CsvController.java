package com.myapt.domain.apt.controller;

import com.myapt.domain.apt.service.CsvService;
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

    // CsvService를 생성자 주입 방식으로 주입받습니다.
    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("uploadV1")
    // @Operation(
    // 	summary = "월 평균매매가 데이터 저장",
    // 	description = "전국 아파트의 월 평균매매가를 저장합니다.",
    // 	security = {},
    // 	responses = {
    // 		@ApiResponse(responseCode = "200", description = "정보 저장완료"),
    // 		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
    //      @ApiResponse(responseCode = "404", description = "항목에 누락이 발생하였습니다."),
    // 		@ApiResponse(responseCode = "500", description = "서버 오류")
    // 	}
    // )
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
    // @Operation(
    // 	summary = "2년치 건설예정 아파트 데이터 저장",
    // 	description = "전국 아파트의 월 평균매매가를 저장합니다.",
    // 	security = {},
    // 	responses = {
    // 		@ApiResponse(responseCode = "200", description = "정보 저장완료"),
    // 		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
    //      @ApiResponse(responseCode = "404", description = "항목에 누락이 발생하였습니다."),
    // 		@ApiResponse(responseCode = "500", description = "서버 오류")
    // 	}
    // )
    public ResponseEntity<String> uploadCsv_v2(@RequestParam("file") MultipartFile file) {
        try {
            // 파일이 비어있거나 null일 경우 400 오류 반환
            if (file.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일이 비어 있습니다.");
            }

            // 서비스 계층을 통해 CSV 데이터를 저장
            csvService.saveCsvDataV2(file);

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
}
