package com.myapt.domain.news.dto;

import lombok.Builder;

@Builder
public record NewsPageRequest(
	Integer num, // 가져올 요소 갯수
	Integer pages, // 페이지 번호
	String sort // 정렬 방식
) {
	public static NewsPageRequest of(Integer pages, Integer num, String sort) {
		return NewsPageRequest.builder()
			.num(num)
			.pages(pages)
			.sort(sort)
			.build();
	}
}