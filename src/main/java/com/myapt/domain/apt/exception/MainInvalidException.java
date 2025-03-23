package com.myapt.domain.apt.exception;

import com.myapt.global.error.exception.InvalidGroupException;

public class MainInvalidException extends InvalidGroupException {
    // Error Code : 400
    public MainInvalidException(String message) {
        super(message);
    }

    // Common
    public static MainInvalidException requestInvalid(){
        return new MainInvalidException("잘못된 요청입니다.");
    }

    // [ 메인화면 공지사항 목록 조회 /apt/notice ]
    public static MainInvalidException sortTypeInvalid(){
        return new MainInvalidException("잘못된 정렬 기준입니다.");
    }
}
