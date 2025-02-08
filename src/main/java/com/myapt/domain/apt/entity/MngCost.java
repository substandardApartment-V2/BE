package com.myapt.domain.apt.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MngCost")
public class MngCost {

    @Id
    @Column(name = "mngcost_id", nullable = false)
    private String id; // 관리비 ID

    @Column(name = "Province")
    private String province; // 도

    @Column(name = "CityCountyDistrict")
    private String cityCountyDistrict; // 시/군/구

    @Column(name = "TownVillage")
    private String townVillage; // 읍/면/동

    @Column(name = "Street")
    private String street; // 거리

    @Column(name = "ComplexName")
    private String complexName; // 단지명

    @Column(name = "OccurrenceYearMonth")
    private Long occurrenceYearMonth; // 발생 연월

    @Column(name = "IndividualUsageSum", nullable = false)
    private Long individualUsageSum; // 개별 사용 합계

    @Column(name = "HeatingCost_Common")
    private Long heatingCostCommon; // 난방비 공용

    @Column(name = "HeatingCost_Individual")
    private Long heatingCostIndividual; // 난방비 개별

    @Column(name = "HotWaterCost_Common")
    private Long hotWaterCostCommon; // 온수비 공용

    @Column(name = "HotWaterCost_Individual")
    private Long hotWaterCostIndividual; // 온수비 개별

    @Column(name = "GasUsageCost_Common")
    private Long gasUsageCostCommon; // 가스비 공용

    @Column(name = "GasUsageCost_Individual")
    private Long gasUsageCostIndividual; // 가스비 개별

    @Column(name = "ElectricityCost_Common")
    private Long electricityCostCommon; // 전기비 공용

    @Column(name = "ElectricityCost_Individual")
    private Long electricityCostIndividual; // 전기비 개별

    @Column(name = "WaterCost_Common")
    private Long waterCostCommon; // 수도비 공용

    @Column(name = "WaterCost_Individual")
    private Long waterCostIndividual; // 수도비 개별

    @Column(name = "TVFee")
    private Long tvFee; // TV 수신료

    @Column(name = "SewageFee")
    private Long sewageFee; // 하수도비

    @Column(name = "WasteFee")
    private Long wasteFee; // 쓰레기 처리비

    @Column(name = "AssociationCost")
    private Long associationCost; // 조합비

    @Column(name = "BuildingInsuranceFee")
    private Long buildingInsuranceFee; // 건물 보험료

    @Column(name = "ElectionCost")
    private Long electionCost; // 선거비

    @Column(name = "etc")
    private Long etc; // 기타 비용

    @Column(name = "ReserveFundMonthlyCharge")
    private Long reserveFundMonthlyCharge; // 예비비 월별 청구액 (장충금)

    @Column(name = "ReserveFundMonthlyExpenditure")
    private Long reserveFundMonthlyExpenditure; // 예비비 월별 지출액

    @Column(name = "ReserveFundTotalAccumulated")
    private Long reserveFundTotalAccumulated; // 예비비 총 누적액

    @Column(name = "ReserveFundAccumulationRate")
    private Long reserveFundAccumulationRate; // 예비비 누적률

    @Column(name = "MiscellaneousIncomeMonthlyAmount")
    private Long miscellaneousIncomeMonthlyAmount; // 잡수입 월별 금액

    @Column(name = "ResidentContributionRevenue")
    private Long residentContributionRevenue; // 주민 기여 수익

    @Column(name = "CommonContributionRevenue")
    private Long commonContributionRevenue; // 공용 기여 수익

    @Column(name = "TotalCommonManagementFeeSum", nullable = false)
    private Long totalCommonManagementFeeSum; // 총 공용 관리비 합계

    @Column(name = "LaborCost")
    private Long laborCost; // 인건비

    @Column(name = "OfficeExpenses")
    private Long officeExpenses; // 사무비

    @Column(name = "TaxesAndDues")
    private Long taxesAndDues; // 세금 및 공과금

    @Column(name = "ClothingCost")
    private Long clothingCost; // 의류비

    @Column(name = "TrainingCost")
    private Long trainingCost; // 교육비

    @Column(name = "VehicleMaintenanceCost")
    private Long vehicleMaintenanceCost; // 차량 유지비

    @Column(name = "OtherIncidentalExpenses")
    private Long otherIncidentalExpenses; // 기타 부대비용

    @Column(name = "CleaningCost")
    private Long cleaningCost; // 청소비

    @Column(name = "SecurityCost")
    private Long securityCost; // 경비비

    @Column(name = "DisinfectionCost")
    private Long disinfectionCost; // 소독비

    @Column(name = "ElevatorMaintenanceCost")
    private Long elevatorMaintenanceCost; // 엘리베이터 유지비

    @Column(name = "IntelligentNetworkMaintenance")
    private Long intelligentNetworkMaintenance; // 지능형 네트워크 유지비

    @Column(name = "RepairCost")
    private Long repairCost; // 수리비

    @Column(name = "FacilityMaintenanceCost")
    private Long facilityMaintenanceCost; // 시설 유지비

    @Column(name = "SafetyInspectionCost")
    private Long safetyInspectionCost; // 안전 검사비

    @Column(name = "DisasterPreventionCost")
    private Long disasterPreventionCost; // 재해 예방비

    @Column(name = "ManagementCommissionFee")
    private Long managementCommissionFee; // 관리 수수료

    // 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detail_apts_id")
    private DetailApts detailApts; // 아파트 상세 ID

}