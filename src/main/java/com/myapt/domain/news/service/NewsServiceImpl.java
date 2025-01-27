package com.myapt.domain.news.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
import com.myapt.domain.news.exception.NewsNullException;
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

	// 30분마다 실행되는 메서드
	@Override
	@Scheduled(fixedRate = 1800000)  // 30분 = 30 * 60 * 1000 밀리초
	public void crawlAndSaveNews() {
		log.info("Starting scheduled news crawling at {}", LocalDateTime.now());

		try {
			// 예시로 특정 키워드로 검색, 필요에 따라 변경 가능
			String keyword = "부동산";
			NewsApiResponse newsApiResponseDto = newsApiResponseRepository.getNewsApiResponseDto(keyword)
				.orElseThrow(NewsNullException::new);
			List<NewsCrawlingResponse> newsResponseDtos = newsApiResponseDto.getNewsResponseDtoWithImages();
			saveNews(newsResponseDtos);
			log.info("Completed news crawling and saving.");
		} catch (Exception e) {
			log.error("Error during scheduled news crawling", e);
		}
	}

	@Override
	public NewsResponse getNews (String keyword, int page, int size, String sort){
		if (page <= 0) {
			throw new NewsNullException();
		}

		Sort sorting = Sort.by("createdAt");
		if ("asc".equalsIgnoreCase(sort)) {
			sorting = sorting.ascending();
		} else {
			sorting = sorting.descending();
		}

		Pageable pageable = PageRequest.of(page - 1, size, sorting);
		Page<News> newsPage = newsRepository.findAll(pageable);
		// 키워드에 맞춰 일반 뉴스인지, 부실 뉴스인지 구분 필요

		if (newsPage.isEmpty()) {
			return new NewsResponse(List.of(), 0);
		}

		List<NewsRequest> newsList = newsPage.getContent().stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());

		return new NewsResponse(newsList, newsPage.getTotalElements());
	}

	private void saveNews(List<NewsCrawlingResponse> newsResponseDtos) {
		List<News> newsList = newsResponseDtos.stream()
			.map(this::mapToNewsEntity)
			.collect(Collectors.toList());
		newsRepository.saveAll(newsList);
	}

	private News mapToNewsEntity(NewsCrawlingResponse dto) {
		return News.builder()
			.platform("Naver")
			.image(dto.getImageLink())
			.title(dto.getTitle())
			.content(dto.getDescription())
			.url(dto.getLink())
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
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
