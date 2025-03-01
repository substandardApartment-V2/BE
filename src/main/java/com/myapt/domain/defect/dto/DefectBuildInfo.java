package com.myapt.domain.defect.dto;

import lombok.Builder;

@Builder
public record DefectBuildInfo(
	String buildStru, // 건물 구조
	String defectType // 부실 사유
) {
	public static DefectBuildInfo of(String buildStru, String defectType) {
		return DefectBuildInfo.builder()
			.buildStru(buildStru)
			.defectType(defectType)
			.build();
	}
}
