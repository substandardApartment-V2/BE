package com.myapt.domain.apt.service;

import org.springframework.web.multipart.MultipartFile;

public interface CsvService {
    void saveCsvDataV1(MultipartFile file);
    void saveCsvDataV2(MultipartFile file);
}
