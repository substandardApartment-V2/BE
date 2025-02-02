package com.myapt.domain.apt.service;

import com.myapt.domain.apt.dto.AptInfoDetail;
import com.myapt.domain.apt.dto.AptInfo;
import com.myapt.domain.apt.dto.MainResponse;
import com.myapt.domain.apt.dto.MngCostInfo;
import com.myapt.domain.apt.dto.NoticeInfo;
import com.myapt.domain.apt.dto.NoticeRequest;
import com.myapt.domain.apt.dto.NoticeResponse;

public interface AptService {
    MainResponse getMainInfo();
    NoticeInfo getNotice(Long id);
    NoticeResponse getNotices(NoticeRequest noticeRequest);
    AptInfo getApartmentInfo(String aptsId);
    AptInfoDetail getApartmentInfoDetail(String detailAptsId);
    MngCostInfo getMngCostInfoDetail(String detailAptsId);
}
