package com.myapt.domain.apt.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record NoticeRequest(
		@NotNull(message = "{pagination.num.notNull}")
		@Min(value = 1, message = "{pagination.num.min}")
		@Max(value = 20, message = "{pagination.num.max}")
		Integer num, // 가져올 요소 갯수

		@NotNull(message = "{pagination.pages.notNull}")
		@Min(value = 0, message = "{pagination.pages.min}")
		Integer pages, // 페이지 번호

		@NotBlank(message = "{pagination.sort.notBlank}")
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
