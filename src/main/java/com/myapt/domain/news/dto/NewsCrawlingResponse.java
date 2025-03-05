package com.myapt.domain.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
@AllArgsConstructor
public class NewsCrawlingResponse {
	private String platform;
	private String title;
	private String link;
	private String description;
	private String imageLink;
	private OffsetDateTime pubDate;

	public boolean validateImageLink() {
		return imageLink != null && !imageLink.isEmpty();
	}
}
