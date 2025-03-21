package com.myapt.domain.news.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Optional;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsoupCrawling {
	/*
	connection을 생성하여 Document를 반환
	 */
	private Document getConnection(String url) throws IOException {
		return Jsoup.connect(url).get();
	}

	/*
	url을 통해 html 문서를 가져오는 메서드
	연결 실패 시, null 반환
	 */
	public Optional<Document> getDocument(String url) {
		try {
			return Optional.of(getConnection(url));
		} catch (MalformedURLException e) {
			log.warn("잘못된 URL 형식: {}. 에러: {}", url, e.getMessage());
		} catch (HttpStatusException e) {
			log.warn("HTTP 상태 오류 코드 ({}): {} URL 요청 중 발생: {}",
				e.getStatusCode(), e.getMessage(), url);
		} catch (UnsupportedMimeTypeException e) {
			log.warn("지원되지 않는 MIME 타입: {}. 에러: {}", url, e.getMessage());
		} catch (SocketTimeoutException e) {
			log.warn("URL 연결 시간 초과: {}. 에러: {}", url, e.getMessage());
		} catch (IOException e) {
			log.warn("URL 연결 중 IO 예외 발생: {}. 에러: {}", url, e.getMessage());
		}
		return Optional.empty();
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
	meta 태그 중 property가 og:image인 태그의 content 속성 값을 가져옴
	해당되는 값이 없는 경우 null 반환
	 */
	public String getImageUrl(Document document) {
		Element image = document.selectFirst("meta[property=og:image]");
		return (image != null) ? image.attr("content") : null;
	}

	/*
		html 문서에서 플랫폼 정보를 추출하는 메서드
		meta 태그 중 name이 twitter:creator인 태그의 content 속성 값을 가져옴
		해당 태그가 없으면 null 반환
	 */
	public String getPlatform(Document document) {
		Element platform = document.selectFirst("meta[name=twitter:creator]");
		return (platform != null) ? platform.attr("content") : null;
	}

	/*
	html 문서에서 본문 내용을 추출하는 메서드
	본문 내용이 최대 길이보다 길면, 나머지 내용은 생략
	 */
	public String getContent(Document document, Integer maxLength) {
		Element content = document.selectFirst("#newsct_article");
		if (content == null) {
			return null;
		}
		String text = content.text();
		if (text.length() > maxLength) {
			text = text.substring(0, maxLength - 3) + "...";
		}
		return text;
	}

	/*
	html 문서에서 제목을 추출하는 메서드
	meta 태그 중 property가 og:title인 태그의 content 속성 값을 가져옴
	해당되는 값이 없는 경우 null 반환
	 */
	public String getTitle(Document document) {
		Element title = document.selectFirst("meta[property=og:title]");
		if (title == null) {
			return null;
		}
		return title.attr("content");
	}
}
