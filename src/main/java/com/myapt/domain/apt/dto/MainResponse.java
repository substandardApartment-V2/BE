package com.myapt.domain.apt.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@JsonPropertyOrder({
		"aptAvgPrice",
		"aptAvgPriceMonth",
		"aptCount",
		"plannedAptCount",
		"plannedAptYear",
		"plannedAptMonth"
})
@Builder
public record MainResponse(
		Long aptAvgPrice,           // 전국 아파트 평균 가격
		Long aptAvgPriceMonth,      // 평균 가격에 해당하는 월
		Long aptCount,              // 전국 아파트 수
		Long plannedAptCount,       // 개발 예정 아파트 수
		Long plannedAptYear,        // 개발 예정 연도 (JSON에서 2025로 정수형)
		Long plannedAptMonth        // 개발 예정 월 (JSON에서 3으로 정수형)
) {
	public static MainResponse of(
			Long aptAvgPrice,
			Long aptAvgPriceMonth, // 미정이라 주석 처리
			Long aptCount,
			Long plannedAptCount,
			Long plannedAptYear,
			Long plannedAptMonth
	) {
		return MainResponse.builder()
				.aptAvgPrice(aptAvgPrice)
				.aptAvgPriceMonth(aptAvgPriceMonth)
				.aptCount(aptCount)
				.plannedAptCount(plannedAptCount)
				.plannedAptYear(plannedAptYear)
				.plannedAptMonth(plannedAptMonth)
				.build();
	}
}