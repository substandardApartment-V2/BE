package com.myapt.domain.apt.dto;

import java.time.LocalDateTime;

import com.myapt.global.util.TimeUtils;

import lombok.Builder;

@Builder
public record NoticeInfo(
	Long id, // 공지 id
	String title, // 공지 제목
	String content, // 공지 내용
	String createAt // 공지 날짜
) {
	public static NoticeInfo of(Long id, String title, String content, LocalDateTime createAt) {
		return NoticeInfo.builder()
			.id(id)
			.title(title)
			.content(content)
			.createAt(TimeUtils.formatTimeDifference(createAt))
			.build();
	}
}
