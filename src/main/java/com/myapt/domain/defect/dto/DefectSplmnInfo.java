package com.myapt.domain.defect.dto;

import lombok.Builder;

@Builder
public record DefectSplmnInfo(
	String reinfStatus, // 보강 상태
	String reinfContent // 보강 내용
) {
	public static DefectSplmnInfo of(String reinfStatus, String reinfContent) {
		return DefectSplmnInfo.builder()
			.reinfStatus(reinfStatus)
			.reinfContent(reinfContent)
			.build();
	}
}
