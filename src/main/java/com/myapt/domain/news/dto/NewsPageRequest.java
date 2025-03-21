package com.myapt.domain.news.dto;

import com.myapt.global.error.annotations.AllowedSortValues;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record NewsPageRequest(
	@NotNull(message = "{pagination.num.notNull}")
	@Min(value = 1, message = "{pagination.num.invalid}")
	@Max(value = 20, message = "{pagination.num.invalid}")
	Integer num, // 가져올 요소 갯수
	@NotNull(message = "{pagination.pages.notNull}")
	@Min(value = 1, message = "{pagination.pages.invalid}")
	Integer pages, // 페이지 번호
	@AllowedSortValues(allowed = {"asc", "desc"})
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