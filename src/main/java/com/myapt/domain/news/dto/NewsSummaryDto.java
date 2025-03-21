package com.myapt.domain.news.dto;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.jsoup.nodes.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.myapt.domain.news.enums.NewsPlatform;
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
	@JsonSetter("originallink")
	private String originalLink;
	@JsonSetter("description")
	private String description;
	@JsonSetter("pubDate")
	@JsonFormat(pattern = "EEE, dd MMM yyyy HH:mm:ss Z", locale = "en")
	private OffsetDateTime pubDate;

	/*
		뉴스 링크를 통해 HTML 문서를 가져와 크롤링을 수행하고
		NewsCrawlingResponse 객체로 변환하는 메서드입니다
		1. 뉴스 링크로 HTML 문서를 가져옴
		2. document가 null일 경우, 기본 데이터(네이버 뉴스 api 결과)로 객체 생성
		3. 정상적으로 로드되면 필요한 정보를 document에서 추출하여 객체 생성
	*/
	public NewsCrawlingResponse toNewsResponseDto(JsoupCrawling jsoupCrawling) {
		// 뉴스 링크를 통해 html 문서 가져옴
		Optional<Document> document = jsoupCrawling.getDocument(link);

		String platform;
		String imageLink = null;
		String newsTitle = title;
		String newsContent = description;

		if (document.isPresent()) {
			// html 문서를 가져온 경우, 문서에서 언론사, 이미지, 제목, 내용을 가져옵니다
			platform = jsoupCrawling.getPlatform(document.get());
			imageLink = jsoupCrawling.getImageUrl(document.get());
			newsTitle = jsoupCrawling.getTitle(document.get());
			newsContent = jsoupCrawling.getContent(document.get(), 255);
		} else {
			// html 문서를 못 가져온 경우, 언론사는 네이버뉴스 url 에서 언론사별 고유한 값을 통해 추출합니다.
			// https://n.news.naver.com/mnews/article/055/0001239771?sid=101 의 경우 055가 언론사 고유값에 해당함
			Optional<NewsPlatform> newsPlatform = NewsPlatform.fromCode(link.split("/")[5]);
			// 만약 NewsPlatform에 없는 값이라면, 언론사 사이트의 도메인을 가져와 임시로 platform에 저장하고
			// 코드 수정을 통해 NewsPlatform에 해당 언론사를 추가합니다
			platform = newsPlatform.map(NewsPlatform::getName)
				.orElseGet(() -> originalLink.split("/")[2]);
		}

		return NewsCrawlingResponse.builder()
			.platform(platform)
			.imageLink(imageLink)
			.title(newsTitle)
			.link(link)
			.description(newsContent)
			.pubDate(pubDate)
			.build();
	}
}
