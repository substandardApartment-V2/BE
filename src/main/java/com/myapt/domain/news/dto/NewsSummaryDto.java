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

	/*
		뉴스 링크를 통해 HTML 문서를 가져와 크롤링을 수행하고
		NewsCrawlingResponse 객체로 변환하는 메서드입니다
		1. 뉴스 링크로 HTML 문서를 가져옴
		2. document가 null일 경우, 기본 데이터(네이버 뉴스 api 결과)로 객체 생성
		3. 정상적으로 로드되면 필요한 정보를 document에서 추출하여 객체 생성
	*/
	public NewsCrawlingResponse toNewsResponseDto(JsoupCrawling jsoupCrawling) {
		// 뉴스 링크를 통해 html 문서 가져옴
		Document document = jsoupCrawling.getDocument(link);
		if (document == null) {
			// document가 null인 경우 기본 값 또는 원본 데이터를 사용하여 응답 생성
			return NewsCrawlingResponse.builder()
				.platform("Naver")
				.imageLink(link)
				.title(title)
				.link(link)
				.description(description)
				.build();
		}

		// html 문서에서 플랫폼 추출
		String extractedPlatform = jsoupCrawling.getPlatform(document);
		// html 문서에서 이미지 url 추출
		String extractedImageLink = jsoupCrawling.getImageUrl(document);
		// html 문서에서 제목 추출
		String extractedTitle = jsoupCrawling.getTitle(document);
		// html 문서에서 본문 추출(최대 길이 설정 가능)
		String extractedContent = jsoupCrawling.getContent(document, 255);

		return NewsCrawlingResponse.builder()
			.platform((extractedPlatform != null) ? extractedPlatform : "Naver")
			.imageLink((extractedImageLink != null) ? extractedImageLink : link)
			.title((extractedTitle != null) ? extractedTitle : title)
			.link(link)
			.description((extractedContent != null) ? extractedContent : description)
			.build();
	}
}
