package com.myapt.domain.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapt.domain.news.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
}