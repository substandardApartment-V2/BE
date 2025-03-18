package com.myapt.domain.apt.exception;

import com.myapt.global.error.exception.NotFoundGroupException;

public class MainNotFoundException extends NotFoundGroupException {
    public MainNotFoundException(String message) {
        super(message);
    }

    // Common
    public static MainNotFoundException requestInvalid(){
        return new MainNotFoundException("잘못된 요청입니다.");
    }

    // [ 메인화면 공지사항 목록 조회 /apt/notice ]
    public static MainNotFoundException noticeNotFound(){
        return new MainNotFoundException("공지사항 조회 중 오류가 발생했습니다.");
    }
    public static MainNotFoundException noticeFetchFailed(){
        return new MainNotFoundException("공지사항 조회 중 오류가 발생했습니다.");
    }
}
