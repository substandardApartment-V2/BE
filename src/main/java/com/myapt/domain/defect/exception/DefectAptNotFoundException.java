package com.myapt.domain.defect.exception;

import com.myapt.global.error.exception.NotFoundGroupException;

public class DefectAptNotFoundException extends NotFoundGroupException {
    public DefectAptNotFoundException(String message) {
        super(message);
    }


    // [ 부실아파트 정보 /defect/info ]
    public static DefectAptNotFoundException defectInfoNotFound() {
        return new DefectAptNotFoundException("부실아파트 정보를 찾을 수 없습니다.");
    }
    public static DefectAptNotFoundException aptInfoNotFound() {
        return new DefectAptNotFoundException("부실아파트 정보와 연관된 아파트 기본정보 엔티티가 없습니다.");
    }
}
