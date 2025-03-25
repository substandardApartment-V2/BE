package com.myapt.domain.news.repository;

import com.myapt.domain.news.entity.News;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NewsJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void batchInsertOrIgnore(List<News> news) {
        String sql = "INSERT INTO news (id, type, platform, image, title, content, url, pub_date, created_at, updated_at) " +
                "VALUES (:id, :type, :platform, :image, :title, :content, :url, :pubDate, :createdAt, :updatedAt) " +
                "ON DUPLICATE KEY UPDATE url = url";

        SqlParameterSource[] params = news.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(sql, params);
    }
}
