package com.myapt.domain.apt.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record NoticeResponse(
	List<NoticeInfo> notices,
	Long totalElements
) {
	public static NoticeResponse of(List<NoticeInfo> notices, Long totalElements) {
		return NoticeResponse.builder()
			.notices(notices)
			.totalElements(totalElements)
			.build();
	}
}
