package com.myapt.domain.apt.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record SearchResponse(
	Long totalElements,
	List<MapSearchInfo> results
) {
	public static SearchResponse of(Long totalElements, List<MapSearchInfo> results) {
		return SearchResponse.builder()
			.totalElements(totalElements)
			.results(results)
			.build();
	}
}
