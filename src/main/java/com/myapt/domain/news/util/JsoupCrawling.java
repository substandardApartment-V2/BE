package com.myapt.domain.news.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Optional;

/*
connection을 생성하여 Elements를 반환
IOException이 발생할 경우 Optional.empty()를 반환하여 NullPointerException을 방지
 */
public class JsoupCrawling {
	public Document getConnection(String url) throws IOException {
		return Jsoup.connect(url).get();
	}

	public Optional<Elements> getJsoupElements(String url, String query) {
		try {
			Document document = getConnection(url);
			return Optional.of(document.select(query));
		} catch (IOException e) {
			return Optional.empty();
		}
	}
}