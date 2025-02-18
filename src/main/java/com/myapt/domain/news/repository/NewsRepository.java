package com.myapt.domain.news.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapt.domain.news.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
	Page<News> findAllByType(Pageable pageable, String type);

	Optional<News> findFirstByTypeOrderByIdDesc(String type);
}
