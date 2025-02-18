package com.myapt.domain.news.exception;

import com.myapt.global.error.exception.NotFoundGroupException;

public class NewsTypeNotFoundException extends NotFoundGroupException {
	public NewsTypeNotFoundException(String message) {
		super(message);
	}

	public NewsTypeNotFoundException() {
		super("해당 뉴스 타입은 존재하지 않습니다.");
	}

}
