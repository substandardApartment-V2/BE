package com.myapt.domain.news.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.myapt.domain.news.util.JsoupCrawling;

import lombok.Getter;

@Getter
public class NewsApiResponse {
	//NewsApiResponseDto는 NewsSummaryDto를 가진 일급 컬렉션으로 설계
	@JsonSetter("items")
	private List<NewsSummaryDto> items;

	//getNaverNews : items로 부터 네이버 뉴스에 등록된 모든 items를 반환합니다.
	public List<NewsSummaryDto> getNaverNews() {
		return items.stream()
			.filter(item -> item.getLink().startsWith("https://n.news"))
			.collect(Collectors.toList());
	}

	/*
	getOnlyNaverNews를 이용하여 필터링 된 뉴스들을 활용 => 다양한 플랫폼 뉴스를 얻기 위해 getNaverNews 제거
	JsoupCrawling 객체를 주입하여 크롤링을 수행한 뒤
	validateImageLink를 통해 이미지가 있는 데이터들로 한번더 필터링하여 => 주석처리함 -> 이미지 없는 기사도 가져오려고
	최대 20개까지 반환합니다.
	 */
	public List<NewsCrawlingResponse> getNewsResponseDtoList() {
		return items.stream()
			.map(item -> item.toNewsResponseDto(new JsoupCrawling()))
			//.filter(NewsResponseDto::validateImageLink)
			.limit(20L)
			.collect(Collectors.toList());
	}
}
