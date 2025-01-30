package com.myapt.domain.apt.dto;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record MngCostInfo(
        // 공용관리비 상세 내역
        List<MonthlyCommonManagementFee> monthlyTotalCommonManagementFeeSum,

        // 개별사용료 상세 내역
        List<MonthlyIndividualManagementFee> monthlyTotalIndividualManagementFeeSum,

        // 장충금 월부과액 상세 내역
        Map<Long, Long> reserveFundMonthlyCharge,

        // 장충금 월사용액 상세 내역
        Map<Long, Long> reserveFundMonthlyExpenditure,

        // 장충금 총적립금액 상세 내역
        Map<Long, Long> reserveFundTotalAccumulated,

        // 장충금 적립율 상세 내역
        Map<Long, Long> reserveFundAccumulationRate,

        // 잡수입 월수입금액 상세 내역
        List<MiscellaneousIncomeMonthlyAmount> miscellaneousIncomeMonthlyAmount
) {

    @Builder
    public static record MonthlyCommonManagementFee(
            long occurrenceYearMonth, // 월
            long laborCost, // 인건비
            long officeExpenses, // 제사무비
            long taxesAndDues, // 제세공과금
            long clothingCost, // 피복비
            long trainingCost, // 교육훈련비
            long vehicleMaintenanceCost, // 차량유지비
            long otherIncidentalExpenses, // 그 밖의 부대비용
            long cleaningCost, // 청소비
            long securityCost, // 경비비
            long disinfectionCost, // 소독비
            long elevatorMaintenanceCost, // 승강기 유지비
            long intelligentNetworkMaintenance, // 지능형 네트워크 유지비
            long repairCost, // 수선비
            long facilityMaintenanceCost, // 시설유지비
            long safetyInspectionCost, // 안전 점검비
            long disasterPreventionCost, // 재해예방비
            long managementCommissionFee // 위탁관리 수수료
    ) {
    }

    @Builder
    public static record MonthlyIndividualManagementFee(
            long occurrenceYearMonth, // 월
            long heatingCostCommon, // 난방비(공용)
            long heatingCostIndividual, // 난방비(전용)
            long hotWaterCostCommon, // 급탕비(공용)
            long hotWaterCostIndividual, // 급탕비(전용)
            long gasUsageCostCommon, // 가스사용료(공용)
            long gasUsageCostIndividual, // 가스사용료(전용)
            long electricityCostCommon, // 전기료(공용)
            long electricityCostIndividual, // 전기료(전용)
            long waterCostCommon, // 수도료(공용)
            long waterCostIndividual, // 수도료(전용)
            long tvFee, // TV 수신료
            long sewageFee, // 정화조 오물 수수료
            long wasteFee, // 생활폐기물 수수료
            long associationCost, // 입대의 운영비
            long buildingInsuranceFee, // 건물 보험료
            long electionCost, // 선관위 운영비
            long etcFee // 기타
    ) {
    }

    @Builder
    public static record MiscellaneousIncomeMonthlyAmount(
            long occurrenceYearMonth, // 월
            long miscellaneousIncomeMonthlyAmount, // 잡수입 월수입금액
            long residentContributionRevenue, // 입주자 기여수익
            long commonContributionRevenue // 공동 기여수익
    ) {
    }
}