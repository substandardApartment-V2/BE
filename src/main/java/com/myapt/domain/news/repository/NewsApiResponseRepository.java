package com.myapt.domain.news.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.myapt.domain.news.dto.NewsApiResponse;
import com.myapt.domain.news.exception.NaverApiCallException;

import lombok.extern.slf4j.Slf4j;

/*
네이버 뉴스에서 NewsApiResponseDto를 가져옵니다.
resttemplate을 통해 Optional로 데이터를 반환 해 줍니다.
통신 코드가 200번대가 아닐 경우 NaverApiCallException을 던집니다.
던진 Exception은 Advice에서 처리합니다.
 */
@Slf4j //log 객체가 정의-> 클래스에 로깅 기능을 추가
@Repository
public class NewsApiResponseRepository {
	@Value("${news.api.secret}")
	private String newsApiKey;

	@Value("${news.api.client}")
	private String newsApiClient;

	public Optional<NewsApiResponse> getNewsApiResponseDto(String keyword) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = getNaverApiRequestHeaders();

			String url = "https://openapi.naver.com/v1/search/news.json?query=" + keyword + "&display=50&sort=date";
			ResponseEntity<NewsApiResponse> newsApiResponseDtoEntity = restTemplate.exchange(url, HttpMethod.GET,
				new HttpEntity<>(headers), NewsApiResponse.class);
			if (newsApiResponseDtoEntity.getStatusCode().is2xxSuccessful()) {
				return Optional.ofNullable(newsApiResponseDtoEntity.getBody());
			}
			throw NaverApiCallException.naverApiCallFailed();
		} catch (NaverApiCallException e) {
			// 예외를 처리합니다. 로그를 남길 수 있습니다.
			log.error("Failed to call Naver API", e); // logger 객체를 생성
			return Optional.empty(); // 실패했을 경우 빈 Optional 반환
		}
	}

	private HttpHeaders getNaverApiRequestHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Naver-Client-Id", newsApiClient);
		headers.set("X-Naver-Client-Secret", newsApiKey);
		return headers;
	}
}
