package com.myapt.domain.apt.dto;

import lombok.Builder;

@Builder
public record NoticeRequest(
	Integer num, // 가져올 요소 갯수
	Integer page, // 페이지 번호
	String sort // 정렬 기준
) {
	public static NoticeRequest of(Integer page, Integer num, String sort) {
		return NoticeRequest.builder()
			.num(num)
			.page(page)
			.sort(sort)
			.build();
	}
}
