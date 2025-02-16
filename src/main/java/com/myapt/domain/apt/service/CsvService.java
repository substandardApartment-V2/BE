package com.myapt.domain.apt.service;

import org.springframework.web.multipart.MultipartFile;

public interface CsvService {
    void saveCsvData(MultipartFile file);
}
