package com.myapt.domain.news.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequest {
	private String platform;
	private String image;
	private String title;
	private String content;
	private String url;
	private LocalDateTime createAt;

	public NewsRequest(String image, String title, String content, String url, LocalDateTime createAt) {
		this.image = image != null ? image : "https://via.placeholder.com/150"; // 기본 이미지 URL 설정
		this.title = title;
		this.content = content;
		this.url = url;
		this.createAt = createAt;
	}
}