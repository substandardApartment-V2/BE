package com.myapt.domain.news.exception;

import com.myapt.global.error.exception.InvalidGroupException;

public class NaverApiCallException extends InvalidGroupException {
	public NaverApiCallException(String message) {
		super(message);
	}

	// Naver News API
	public static NaverApiCallException naverApiCallFailed() {
		return new NaverApiCallException("네이버 API 오류");
	}
}
