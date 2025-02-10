package com.myapt.domain.apt.exception;

public class MarkerNotFoundException extends NoticeNotFoundException{
	public MarkerNotFoundException(String message) {
		super(message);
	}

	public MarkerNotFoundException() {
		super("해당 좌표 내 마커가 존재하지 않습니다.");
	}
}
