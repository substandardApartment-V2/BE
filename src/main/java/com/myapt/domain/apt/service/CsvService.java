package com.myapt.domain.apt.service;

import org.springframework.web.multipart.MultipartFile;

public interface CsvService {
    void saveCsvDataV1(MultipartFile file);
    void downloadAndSaveCsvDataV2(String baseUrl);
}
