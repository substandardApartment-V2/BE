package com.myapt.domain.news.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record NewsResponse (
	List<NewsRequest> newsList,
	long totalElements
) {
	public static NewsResponse of(List<NewsRequest> newsList, long totalElements) {
		return NewsResponse.builder()
			.newsList(newsList)
			.totalElements(totalElements)
			.build();
	}
}
