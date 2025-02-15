package com.myapt.domain.defect.dto;

import lombok.Builder;

@Builder
public record DefectSplmnInfo(
	String reinfContent, // 보강 내용
	String reinfStatus // 보강 상태
) {
	public static DefectSplmnInfo of(String reinfContent, String reinfStatus) {
		return DefectSplmnInfo.builder()
			.reinfContent(reinfContent)
			.reinfStatus(reinfStatus)
			.build();
	}
}
