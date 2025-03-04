package com.myapt.domain.news.entity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class News {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String type; // 뉴스 타입 (일반 부동산 뉴스, 부실 부동산 뉴스)

	private String platform; // 뉴스 플랫폼

	private String image; // 뉴스 이미지

	private String title; // 뉴스 타이틀

	private String content; // 뉴스 본문

	private String url; // 뉴스 url

	private OffsetDateTime pubDate; // 뉴스 출판 날짜

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	//기타
	@Builder
	private News(String type, String platform, String image, String title, String content, String url,
		LocalDateTime createdAt, LocalDateTime updatedAt, OffsetDateTime pubDate) {
		this.type = type;
		this.platform = platform;
		this.image = image;
		this.title = title;
		this.content = content;
		this.url = url;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.pubDate = pubDate;
	}

	public LocalDateTime getCreateAt() {
		return createdAt;
	}
}
