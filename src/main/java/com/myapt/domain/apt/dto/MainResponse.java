package com.myapt.domain.apt.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@JsonPropertyOrder({"aptAvgPrice", "aptCount", "plannedAptCount"})
@Builder
public record MainResponse(
		Long aptAvgPrice,
		Long aptCount,
		Long plannedAptCount
) {
	public static MainResponse of(Long aptAvgPrice, Long aptCount, Long plannedAptCount) {
		return MainResponse.builder()
				.aptAvgPrice(aptAvgPrice)
				.aptCount(aptCount)
				.plannedAptCount(plannedAptCount)
				.build();
	}
}
