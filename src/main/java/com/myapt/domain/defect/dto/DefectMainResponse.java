package com.myapt.domain.defect.dto;

import lombok.Builder;

@Builder
public record DefectMainResponse (
	String platform,
	String image,
	String title,
	String content,
	String url,
	String createdAt
) {
	public static DefectMainResponse of(String platform, String image, String title, String content, String url, String createdAt) {
		return DefectMainResponse.builder()
			.platform(platform)
			.image(image)
			.title(title)
			.content(content)
			.url(url)
			.createdAt(createdAt)
			.build();
	}
}
