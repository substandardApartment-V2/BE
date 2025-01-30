package com.myapt.domain.apt.service;

import com.myapt.domain.apt.dto.AptInfoDetail;
import com.myapt.domain.apt.dto.AptInfo;
import com.myapt.domain.apt.dto.MngCostInfo;

public interface AptService {
    AptInfo getApartmentInfo(String aptsId);
    AptInfoDetail getApartmentInfoDetail(String detailAptsId);
    MngCostInfo getMngCostInfoDetail(String detailAptsId);
}
