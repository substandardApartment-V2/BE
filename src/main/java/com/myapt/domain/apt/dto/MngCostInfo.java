package com.myapt.domain.apt.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record MngCostInfo(
        String year,                                 // 연도
        List<MonthlyFee> individualUsageSum,         // 개별사용료 상세 내역
        List<MonthlyFee> totalCommonManagementFeeSum,// 공용관리비 월별 데이터
        List<MonthlyFee> reserveFundMonthlyCharge    // 장충금 월 부과액
) {

    @Builder
    public static record MonthlyFee(
            String month, // 월
            long fee      // 비용
    ) {
    }
}
