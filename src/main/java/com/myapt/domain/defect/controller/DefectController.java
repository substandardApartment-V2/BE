package com.myapt.domain.defect.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapt.domain.defect.dto.DefectInfoResponse;
import com.myapt.domain.defect.service.DefectService;
import com.myapt.domain.news.dto.NewsResponse;
import com.myapt.domain.news.service.NewsServiceImpl;
import com.myapt.global.template.ResTemplate;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/defect")
// @Tag(name = "부실 아파트", description = "부실 아파트 관련 API 그룹")
public class DefectController {
	private final NewsServiceImpl newsService;
	private final DefectService defectService;

	@GetMapping("/main")
	// @Operation(
	// 	summary = "부실 아파트 목록 조회",
	// 	description = "부실 아파트 목록을 조회합니다.",
	// 	security = {},
	// 	responses = {
	// 		@ApiResponse(responseCode = "200", description = "부실 아파트 목록 조회 성공"),
	// 		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
	// 		@ApiResponse(responseCode = "500", description = "서버 오류")
	// 	}
	// )
	public ResTemplate<NewsResponse> getDefectMain() {
		NewsResponse data = newsService.getNews("부실 아파트", 1, 3, "desc");
		return new ResTemplate<>(HttpStatus.OK, "부실 아파트 메인 조회 성공", data);
	}

	@GetMapping("/info")
	// @Operation(
	// 	summary = "부실 아파트 정보 조회",
	// 	description = "부실 아파트 정보를 조회합니다.",
	// 	security = {},
	// 	responses = {
	// 		@ApiResponse(responseCode = "200", description = "부실 아파트 정보 조회 성공"),
	// 		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
	// 		@ApiResponse(responseCode = "404", description = "해당 아파트 정보가 존재하지 않습니다."),
	// 		@ApiResponse(responseCode = "500", description = "서버 오류")
	// 	}
	// )
	public ResTemplate<DefectInfoResponse>  getInfoDefectApt(Long id) {
		DefectInfoResponse data = defectService.getInfoDefectApt(id);
		return new ResTemplate<>(HttpStatus.OK, "부실 아파트 정보 조회 성공", data);
	}
}