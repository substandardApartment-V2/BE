package com.myapt.domain.defect.dto;

import lombok.Builder;

@Builder
public record DefectInfoResponse (
	DefectAptInfo aptInfo,
	DefectBuildInfo buildInfo,
	DefectSplmnInfo splmnInfo
) {
	public static DefectInfoResponse of(
		DefectAptInfo aptInfo, DefectBuildInfo buildInfo, DefectSplmnInfo splmnInfo) {
		return DefectInfoResponse.builder()
			.aptInfo(aptInfo)
			.buildInfo(buildInfo)
			.splmnInfo(splmnInfo)
			.build();
	}
}
