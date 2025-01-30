package com.myapt.domain.apt.service;

import com.myapt.domain.apt.dto.AptInfo;

public interface AptService {
    AptInfo getApartmentInfo(String aptsId);
}
