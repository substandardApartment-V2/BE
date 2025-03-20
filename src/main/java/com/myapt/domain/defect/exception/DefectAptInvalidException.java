package com.myapt.domain.defect.exception;

import com.myapt.global.error.exception.InvalidGroupException;

public class DefectAptInvalidException extends InvalidGroupException {
    public DefectAptInvalidException(String message) {
        super(message);
    }

    // Common
    public static DefectAptInvalidException requestInvalid(){
        return new DefectAptInvalidException("잘못된 요청입니다.");
    }
    public static DefectAptInvalidException pageInfoInvalid(){
        return new DefectAptInvalidException("잘못된 페이지 정보입니다.");
    }
    public static DefectAptInvalidException numInvalid(){
        return new DefectAptInvalidException("잘못된 조회 갯수 요청입니다.");
    }
    public static DefectAptInvalidException sortTypeInvalid(){
        return new DefectAptInvalidException("잘못된 정렬 기준입니다.");
    }

    // [ 부실아파트 메인 /defect/main ]

}
