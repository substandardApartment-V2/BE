package com.myapt.domain.news.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping("/{type}")
	public ResTemplate<NewsResponse> getNews(
			@PathVariable String type,
			@RequestParam("pages") int pages,
			@RequestParam("sort") String sort) {
		NewsResponse data;
		if (type.equals("general")) {
			data = newsService.getNews("부실 아파트", pages, 8, sort);
		} else {
			data = newsService.getNews("부실 아파트", pages, 8, sort);
		}
		return new ResTemplate<>(HttpStatus.OK, "뉴스 조회 성공", data);
	}
}
