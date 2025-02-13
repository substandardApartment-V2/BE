package com.myapt.domain.apt.dto;

import lombok.Builder;

@Builder
public record NoticeRequest(
	Integer num, // 가져올 요소 갯수
	Integer pages, // 페이지 번호
	String sort // 정렬 기준
) {
	public static NoticeRequest of(Integer pages, Integer num, String sort) {
		return NoticeRequest.builder()
			.num(num)
			.pages(pages)
			.sort(sort)
			.build();
	}
}
