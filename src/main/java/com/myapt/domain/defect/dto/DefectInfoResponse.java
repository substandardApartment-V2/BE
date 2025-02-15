package com.myapt.domain.defect.dto;

import lombok.Builder;

@Builder
public record DefectInfoResponse (
	DefectAptInfo aptInfo,
	DefectBasicInfo basicInfo,
	DefectBuildInfo buildInfo,
	DefectSplmnInfo splmnInfo
) {
	public static DefectInfoResponse of(
		DefectAptInfo aptInfo, DefectBasicInfo basicInfo, DefectBuildInfo buildInfo, DefectSplmnInfo splmnInfo) {
		return DefectInfoResponse.builder()
			.aptInfo(aptInfo)
			.basicInfo(basicInfo)
			.buildInfo(buildInfo)
			.splmnInfo(splmnInfo)
			.build();
	}
}
