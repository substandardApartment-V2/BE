package com.myapt.domain.apt.dto;

import lombok.Builder;

@Builder
public record MainResponse(
	Long aptAvgPrice,
	Long aptCount,
	Long plannedAptCount,
	LowestMgmtFeeAptInfo lowestMgmtFeeAptInfo

) {
	public static MainResponse of(Long aptAvgPrice, Long aptCount, Long plannedAptCount, LowestMgmtFeeAptInfo lowestMgmtFeeAptInfo) {
		return MainResponse.builder()
			.aptAvgPrice(aptAvgPrice)
			.aptCount(aptCount)
			.plannedAptCount(plannedAptCount)
			.lowestMgmtFeeAptInfo(lowestMgmtFeeAptInfo)
			.build();
	}
}
