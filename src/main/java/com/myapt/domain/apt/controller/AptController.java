package com.myapt.domain.apt.controller;

import com.myapt.domain.apt.dto.AptInfo;
import com.myapt.domain.apt.service.AptService;
import com.myapt.global.template.ResTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
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
}
