package com.myapt.domain.news.exception;

import com.myapt.global.error.exception.NotFoundGroupException;

public class NewsNotFoundException extends NotFoundGroupException {
    public NewsNotFoundException(String message) {
        super(message);
    }

    // Common
    public static NewsNotFoundException sortTypeNotFound() {
        return new NewsNotFoundException("해당 정렬 타입은 존재하지 않습니다.");
    }

    public static NewsNotFoundException newsNotFound() {
        return new NewsNotFoundException("해당 뉴스 정보가 존재하지 않습니다.");
    }

    // [ 뉴스 조회 /news ]
    public static NewsNotFoundException newsTypeNotFound() {
        return new NewsNotFoundException("해당 뉴스 타입은 존재하지 않습니다.");
    }
}
