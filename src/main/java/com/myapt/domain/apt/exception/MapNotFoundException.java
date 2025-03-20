package com.myapt.domain.apt.exception;

import com.myapt.global.error.exception.NotFoundGroupException;

public class MapNotFoundException extends NotFoundGroupException {
    public MapNotFoundException(String message) {
        super(message);
    }

    // Common
    public static MapNotFoundException mapNotFound(){
        return new MapNotFoundException("지도 조회 중 오류가 발생했습니다.");
    }

    // [ 지도 좌표(지도 마커 불러오기) /map ]
    public static MapNotFoundException typeInvalid(String type){
        return new MapNotFoundException(String.format("%s에 해당하는 리소스(경로)가 존재하지 않습니다.", type));
    }
}
