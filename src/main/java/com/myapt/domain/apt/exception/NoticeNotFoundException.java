package com.myapt.domain.apt.exception;

import com.myapt.global.error.exception.NotFoundGroupException;

public class NoticeNotFoundException extends NotFoundGroupException {
	public NoticeNotFoundException(String message) {
		super(message);
	}

	public NoticeNotFoundException() {
		super("가져올 공지사항이 존재하지 않습니다.");
	}
}
