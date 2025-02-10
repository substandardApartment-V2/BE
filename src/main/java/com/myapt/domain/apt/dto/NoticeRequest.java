package com.myapt.domain.apt.dto;

import lombok.Builder;

@Builder
public record NoticeRequest(
	Integer num, // 가져올 요소 갯수
	Integer pages // 페이지 번호
) {
	public static NoticeRequest of(Integer pages, Integer num) {
		return NoticeRequest.builder()
			.num(num)
			.pages(pages)
			.build();
	}
}
