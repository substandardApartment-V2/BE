package com.myapt.domain.news.util;

import java.io.IOException;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/*
connection을 생성하여 Elements를 반환
IOException이 발생할 경우 Optional.empty()를 반환하여 NullPointerException을 방지
 */
public class JsoupCrawling {
	private Document getConnection(String url) throws IOException {
		return Jsoup.connect(url).get();
	}

	/*
	url을 통해 html 문서를 가져오는 메서드
	연결 실패 시, null 반환
	 */
	public Document getDocument(String url) {
		try {
			return getConnection(url);
		} catch (IOException e) {
			return null;
		}
	}

	public Optional<Elements> getJsoupElements(String url, String query) {
		try {
			Document document = getConnection(url);
			return Optional.of(document.select(query));
		} catch (IOException e) {
			return Optional.empty();
		}
	}

	/*
	html 문서에서 이미지를 추출하는 메서드
	id가 contents인 태그 안에 있는 img 태그의 속성 data-src의 값을 가져옴
	해당되는 값이 없는 경우 null 반환
	 */
	public String getImageUrl(Document document) {
		String imageUrl = document.select("#contents img").attr("data-src");
		return imageUrl.isBlank() ? null : imageUrl;
	}

	/*
	html 문서에서 본문 내용을 추출하는 메서드
	본문 내용이 최대 길이보다 길면, 나머지 내용은 생략
	 */
	public String getContent(Document document, Integer maxLength) {
		String text = document.text();
		if (text.length() > maxLength) {
			text = text.substring(0, maxLength - 3) + "...";
		}
		return text;
	}
}
