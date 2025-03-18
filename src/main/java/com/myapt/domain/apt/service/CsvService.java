package com.myapt.domain.apt.service;

public interface CsvService {

    // 전국 월별 건축 예정 아파트 수 저장
    void downloadAndSaveCsvDataV2(String baseUrl);

    // 전국 아파트 평균 매매가 저장
    void downloadAndSaveAveragePriceData(String targetUrl);




}
