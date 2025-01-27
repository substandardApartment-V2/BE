package com.myapt.domain.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NewsCrawlingResponse {
	private String title;
	private String link;
	private String description;
	private String imageLink;

	public boolean validateImageLink() {
		return imageLink != null && !imageLink.isEmpty();
	}
}