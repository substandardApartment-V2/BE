package com.myapt.domain.news.exception;

import com.myapt.global.error.exception.NotFoundGroupException;

public class NewsNullException extends NotFoundGroupException {
	public NewsNullException(String message) {
		super(message);
	}

	public NewsNullException() {
		super("해당 뉴스 정보가 존재하지 않습니다.");
	}
}
