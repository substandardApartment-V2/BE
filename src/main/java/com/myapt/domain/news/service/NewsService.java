package com.myapt.domain.news.service;

import com.myapt.domain.news.dto.NewsResponse;

public interface NewsService {
	public void crawlAndSaveNews();
	public NewsResponse getNews(String keyword, int page, int size, String sort);
}
