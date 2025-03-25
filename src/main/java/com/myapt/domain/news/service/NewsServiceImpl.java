package com.myapt.domain.news.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.myapt.domain.news.repository.NewsJdbcRepository;
import com.myapt.domain.defect.exception.DefectAptInvalidException;
import com.myapt.domain.news.exception.NewsNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.myapt.domain.news.dto.NewsApiResponse;
import com.myapt.domain.news.dto.NewsCrawlingResponse;
import com.myapt.domain.news.dto.NewsRequest;
import com.myapt.domain.news.dto.NewsResponse;
import com.myapt.domain.news.entity.News;
import com.myapt.domain.news.repository.NewsApiResponseRepository;
import com.myapt.domain.news.repository.NewsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

	private final NewsApiResponseRepository newsApiResponseRepository;
	private final NewsRepository newsRepository;
	private final NewsJdbcRepository newsJdbcRepository;

	/*
	 30분마다 실행되는 스케줄러 메서드
	 부실 뉴스와 일반 뉴스를 주기적으로 크롤링하여 DB에 저장함
	 */
	@Override
	@Scheduled(fixedRate = 1800000)  // 30분 = 30 * 60 * 1000 밀리초
	public void crawlAndSaveNews() {
		log.info("Starting scheduled news crawling at {}", LocalDateTime.now());

		try {
			processDefectNews();
			processNormalNews();
		} catch (Exception e) {
			log.error("Error during scheduled news crawling", e);
		}
	}

	/*
	 부실(Defect) 뉴스 처리
	 1. 부실 뉴스 API 조회
	 2. DB에 뉴스 url 기준으로 중복 없이 부실 아파트 뉴스 저장
	 */
	private void processDefectNews() {
		String defectKeyword = "아파트 부실 시공 공사";

		// 부실 뉴스 조회 - 가장 최근 뉴스가 첫번째, 가장 오래된 뉴스가 마지막
		NewsApiResponse defectNewsApiResponse = newsApiResponseRepository.getNewsApiResponseDto(defectKeyword)
				.orElseThrow(NewsNotFoundException::newsNotFound);
		List<NewsCrawlingResponse> defectNewsList = defectNewsApiResponse.getNewsResponseDtoList();

		saveNews(defectNewsList, "부실 아파트");
		log.info("Completed defect news crawling and saving.");
	}

	/*
	 일반 뉴스 처리
	 1. 일반 뉴스 API 조회
	 2. DB에 뉴스 url 기준으로 중복 없이 일반 뉴스 저장
	 */
	private void processNormalNews() {
		String normalKeyword = "아파트 부동산";

		// 일반 뉴스 조회
		NewsApiResponse normalNewsApiResponse = newsApiResponseRepository.getNewsApiResponseDto(normalKeyword)
				.orElseThrow(NewsNotFoundException::newsNotFound);
		List<NewsCrawlingResponse> normalNewsList = normalNewsApiResponse.getNewsResponseDtoList();

		// 일반 뉴스 DB 저장
		saveNews(normalNewsList, "아파트");
		log.info("Completed normal news crawling and saving.");
	}

	/**
	 * 뉴스 조회
	 * @param keyword 키워드
	 * @param page 페이지 번호 (1부터 시작)
	 * @param size 페이지 크기 (1~20)
	 * @param sort 정렬 방식 (asc, desc)
	 * @return 뉴스 리스트와 전체 뉴스 개수
	 */
	@Override
	public NewsResponse getNews(String keyword, int page, int size, String sort) {
		if (page <= 0) {
			throw DefectAptInvalidException.pageInfoInvalid();
		}
		if (size <= 0 || size > 20) {
			throw DefectAptInvalidException.numInvalid();
		}

		Sort sorting = Sort.by("createdAt");
		if ("asc".equalsIgnoreCase(sort)) {
			sorting = sorting.ascending();
		} else if ("desc".equalsIgnoreCase(sort)) {
			sorting = sorting.descending();
		} else {
			throw DefectAptInvalidException.sortTypeInvalid();
		}

		Pageable pageable = PageRequest.of(page - 1, size, sorting);
		// 키워드에 맞춰 일반 뉴스인지, 부실 뉴스인지 구분
		Page<News> newsPage = newsRepository.findAllByType(pageable, keyword);

		if (newsPage.isEmpty()) {
			return new NewsResponse(List.of(), 0);
		}

		List<NewsRequest> newsList = newsPage.getContent().stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());

		return new NewsResponse(newsList, newsPage.getTotalElements());
	}

	private void saveNews(List<NewsCrawlingResponse> newsResponseDtos, String newsType) {
		List<News> newsList = newsResponseDtos.stream()
				.map(news -> mapToNewsEntity(news, newsType))
				.collect(Collectors.toList());
		newsJdbcRepository.batchInsertOrIgnore(newsList);
	}

	private News mapToNewsEntity(NewsCrawlingResponse dto, String type) {
		return News.builder()
				.type(type)
				.platform(dto.getPlatform())
				.image(dto.getImageLink())
				.title(dto.getTitle())
				.content(dto.getDescription())
				.url(dto.getLink())
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.pubDate(dto.getPubDate())
				.build();
	}

	private NewsRequest convertToDTO(News news) {
		return NewsRequest.builder()
				.platform(news.getPlatform())
				.image(news.getImage())
				.title(news.getTitle())
				.content(news.getContent())
				.url(news.getUrl())
				.createAt(news.getCreateAt())
				.build();
	}
}