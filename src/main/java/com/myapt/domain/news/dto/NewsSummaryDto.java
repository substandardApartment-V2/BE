package com.myapt.domain.news.dto;

import org.jsoup.nodes.Document;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.myapt.domain.news.util.JsoupCrawling;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class NewsSummaryDto {

	@JsonSetter("title")
	private String title;
	@JsonSetter("link")
	private String link;
	@JsonSetter("description")
	private String description;

	public NewsCrawlingResponse toNewsResponseDto(JsoupCrawling jsoupCrawling) {
		// 뉴스 링크를 통해 html 문서 가져옴
		Document document = jsoupCrawling.getDocument(link);
		// html 문서에서 이미지 url 추출
		String imageLink = jsoupCrawling.getImageUrl(document);
		// html 문서에서 본문 추출(최대 길이 설정 가능)
		String content = jsoupCrawling.getContent(document, 250);
		content = content.length() > description.length() ? content : description;

		return NewsCrawlingResponse.builder()
			.imageLink(imageLink)
			.title(title)
			.link(link)
			.description(content)
			.build();
	}
}
