package com.myapt.domain.news.controller;

import com.myapt.domain.news.exception.NewsNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapt.domain.news.dto.NewsPageRequest;
import com.myapt.domain.news.dto.NewsResponse;
import com.myapt.domain.news.service.NewsServiceImpl;
import com.myapt.global.template.ResTemplate;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*") // 향후 도메인 주소 변경 필요
@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
	private final NewsServiceImpl newsService;

	@PostMapping("/{type}")
	public ResTemplate<NewsResponse> getNews(
		@PathVariable String type,
		@RequestBody NewsPageRequest newsPageRequest) {
		NewsResponse data;
		if (type.equals("apt")) {
			data = newsService.getNews("아파트", newsPageRequest.pages(), newsPageRequest.num(), newsPageRequest.sort());
		} else if (type.equals("defect")) {
			data = newsService.getNews("부실 아파트", newsPageRequest.pages(), newsPageRequest.num(),
				newsPageRequest.sort());
		} else {
			throw NewsNotFoundException.newsTypeNotFound();
		}
		return new ResTemplate<>(HttpStatus.OK, "뉴스 조회 성공", data);
	}
}
