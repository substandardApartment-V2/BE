package com.myapt.domain.defect.exception;

import com.myapt.global.error.exception.NotFoundGroupException;

public class AptNotFoundException extends NotFoundGroupException {
	public AptNotFoundException(String message) {
		super(message);
	}

	public AptNotFoundException() {
		super("해당 아파트 정보가 존재하지 않습니다.");
	}
}
