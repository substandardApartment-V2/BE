package com.myapt.domain.news.dto;

import java.util.Optional;

import org.jsoup.select.Elements;

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
		String query = "#contents img";
		Optional<Elements> jsoupElements = jsoupCrawling.getJsoupElements(link, query);
		if (jsoupElements.isPresent()) {
			return NewsCrawlingResponse.builder()
				.imageLink(jsoupElements.get().attr("data-src"))
				.title(title)
				.link(link)
				.description(description)
				.build();
		}
		return NewsCrawlingResponse.builder()
			.title(title)
			.link(link)
			.description(description)
			.build();
	}
}
