package com.myapt.domain.apt.exception;

import com.myapt.global.error.exception.InvalidGroupException;

public class MapInvalidException extends InvalidGroupException {
    public MapInvalidException(String message) {
        super(message);
    }

    // Common
    public static MapInvalidException requestInvalid(){
        return new MapInvalidException("잘못된 요청입니다.");
    }

    // [ 지도 좌표(지도 마커 불러오기) /map ]
    public static MapInvalidException locationInvalid(){
        return new MapInvalidException("잘못된 좌표 정보입니다.");
    }


    // [ 지도 검색 /map/search ]
    public static MapInvalidException pageInfoInvalid(){
        return new MapInvalidException("잘못된 페이지 정보입니다.");
    }
    public static MapInvalidException numInvalid(){
        return new MapInvalidException("잘못된 조회 갯수 요청입니다.");
    }
    public static MapInvalidException keywordInvalid(){
        return new MapInvalidException("잘못된 검색어입니다.");
    }

}
