package com.myapt.domain.apt.controller;

import com.myapt.domain.apt.dto.AptInfo;
import com.myapt.domain.apt.dto.AptInfoDetail;
import com.myapt.domain.apt.dto.MainResponse;
import com.myapt.domain.apt.dto.MngCostInfo;
import com.myapt.domain.apt.dto.NoticeInfo;
import com.myapt.domain.apt.dto.NoticeRequest;
import com.myapt.domain.apt.dto.NoticeResponse;
import com.myapt.domain.apt.service.AptService;
import com.myapt.global.template.ResTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apt")
public class AptController {
    private final AptService aptService;

    // AptService를 생성자 주입 방식으로 주입받습니다.
    public AptController(AptService aptService) {
        this.aptService = aptService;
    }

    @GetMapping("main")
    // @Operation(
    // 	summary = "메인화면 조회",
    // 	description = "메인화면 정보를 조회합니다.",
    // 	security = {},
    // 	responses = {
    // 		@ApiResponse(responseCode = "200", description = "메인화면 정보 로딩완료"),
    // 		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
    //      @ApiResponse(responseCode = "404", description = "항목에 누락이 발생하였습니다."),
    // 		@ApiResponse(responseCode = "500", description = "서버 오류")
    // 	}
    // )
    public ResTemplate<MainResponse> getMainInfo() {
        MainResponse data = aptService.getMainInfo();
        return new ResTemplate<>(HttpStatus.OK, "메인화면 조회 성공", data);
    }

    @PostMapping("/notice")
    // @Operation(
    // 	summary = "공지사항 목록 조회",
    // 	description = "공지사항 목록을 조회합니다..",
    // 	security = {},
    // 	responses = {
    // 		@ApiResponse(responseCode = "200", description = "공지사항 목록 로딩완료"),
    // 		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
    //      @ApiResponse(responseCode = "404", description = "공지사항이 존재하지 않습니다."),
    // 		@ApiResponse(responseCode = "500", description = "서버 오류")
    // 	}
    // )
    public ResTemplate<NoticeResponse> getNoticeList(@RequestBody NoticeRequest noticeRequest) {
        NoticeResponse data = aptService.getNotices(noticeRequest);
        return new ResTemplate<>(HttpStatus.OK, "공지사항 조회 성공", data);
    }

    @GetMapping("/notice/{id}")
    // @Operation(
    // 	summary = "공지사항 조회",
    // 	description = "공지사항를 조회합니다..",
    // 	security = {},
    // 	responses = {
    // 		@ApiResponse(responseCode = "200", description = "공지사항 로딩완료"),
    // 		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
    //      @ApiResponse(responseCode = "404", description = "해당 공지사항이 존재하지 않습니다."),
    // 		@ApiResponse(responseCode = "500", description = "서버 오류")
    // 	}
    // )
    public ResTemplate<NoticeInfo> getNoticeList(@PathVariable Long id) {
        NoticeInfo data = aptService.getNotice(id);
        return new ResTemplate<>(HttpStatus.OK, "공지사항 조회 성공", data);
    }

    @GetMapping("/info")
    // @Operation(
    // 	summary = "특정 아파트 기본 정보 조회",
    // 	description = "선택한 아파트 기본 정보를 조회합니다.",
    // 	security = {},
    // 	responses = {
    // 		@ApiResponse(responseCode = "200", description = "선택한 아파트 기본 정보 조회 성공"),
    // 		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
    // 		@ApiResponse(responseCode = "500", description = "서버 오류")
    // 	}
    // )
    public ResTemplate<AptInfo> getAptInfo(@RequestParam String aptsId) {
        AptInfo aptInfo = aptService.getApartmentInfo(aptsId);
        return new ResTemplate<>(HttpStatus.OK, "아파트 기본 정보 조회 성공", aptInfo);
    }

    @GetMapping("/detail")
    // @Operation(
    // 	summary = "특정 아파트 정보 상세조회",
    // 	description = "아파트 정보를 상세조회합니다.",
    // 	security = {},
    // 	responses = {
    // 		@ApiResponse(responseCode = "200", description = "특정 아파트 정보 상세 조회 성공"),
    // 		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
    // 		@ApiResponse(responseCode = "404", description = "해당 아파트 상세 정보가 존재하지 않습니다."),
    // 		@ApiResponse(responseCode = "500", description = "서버 오류")
    // 	}
    // )
    public ResTemplate<AptInfoDetail> getAptInfoDetail(@RequestParam String detail_apts_id) { //아파트 관리비 코드(기본키)로 조회
        AptInfoDetail aptInfoDetail = aptService.getApartmentInfoDetail(detail_apts_id);
        return new ResTemplate<>(HttpStatus.OK, "아파트 상세 정보 조회 성공", aptInfoDetail);
    }

    @GetMapping("/feeDetail")
    // @Operation(
    // 	summary = "특정 아파트 관리비 정보 상세조회",
    // 	description = "관리비 정보를 상세조회합니다.",
    // 	security = {},
    // 	responses = {
    // 		@ApiResponse(responseCode = "200", description = "관리비 정보 상세 조회 성공"),
    // 		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
    // 		@ApiResponse(responseCode = "404", description = "해당 관리비 상세 정보가 존재하지 않습니다."),
    // 		@ApiResponse(responseCode = "500", description = "서버 오류")
    // 	}
    // )
    public ResTemplate<MngCostInfo> getMngCostInfo(@RequestParam String detail_apts_id) { //아파트 관리비 코드(기본키)로 조회
        MngCostInfo mngCostInfo = aptService.getMngCostInfoDetail(detail_apts_id);
        return new ResTemplate<>(HttpStatus.OK, "관리비 상세 정보 조회 성공", mngCostInfo);
    }

    // 구성요소가 빠졌을 경우 400으로 처리하기 위해서 생성
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResTemplate<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ResTemplate<Void> response = new ResTemplate<>(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
