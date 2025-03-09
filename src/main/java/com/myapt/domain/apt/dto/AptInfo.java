package com.myapt.domain.apt.dto;

import lombok.Builder;

import java.util.List;

public record AptInfo(
        AptBasicInfo aptInfo,
        BuildInfo buildInfo,
        MonthlyMaintenanceFees monthlyMaintenanceFees,
        EtcInfo etcInfo
) {
    @Builder
    public static AptInfo of(
            String detailId, String name, String roadAddress, String zipCode,
            String completionDate, String developer, String constructor, Long numberOfUnits,
            String year, List<MonthlyMaintenanceData> monthlyMaintenanceData,
            List<String> amenities, String buildingStructure, String managementType, String heatingType,
            Long cctvCount, Long totalParkingSpaces, Long totalGroundEvChargerCount,
            String managementOfficeAddress, String managementOfficeContact, String managementOfficeFax,
            String housingManager
    ) {
        return new AptInfo(
                new AptBasicInfo(detailId, name, roadAddress, zipCode),
                new BuildInfo(completionDate, developer, constructor, numberOfUnits),
                new MonthlyMaintenanceFees(year, monthlyMaintenanceData),
                new EtcInfo(amenities, buildingStructure, managementType, heatingType, cctvCount, totalParkingSpaces,
                        totalGroundEvChargerCount, managementOfficeAddress, managementOfficeContact,
                        managementOfficeFax, housingManager)
        );
    }

    public record AptBasicInfo(String detailId, String name, String roadAddress, String zipCode) {}
    public record BuildInfo(String completionDate, String developer, String constructor, Long numberOfUnits) {}
    public record MonthlyMaintenanceFees(String year, List<MonthlyMaintenanceData> data) {}
    public record MonthlyMaintenanceData(String month, Long fee) {}
    public record EtcInfo(
            List<String> amenities, String buildingStructure, String managementType, String heatingType,
            Long cctvCount, Long totalParkingSpaces, Long totalGroundEvChargerCount,
            String managementOfficeAddress, String managementOfficeContact, String managementOfficeFax,
            String housingManager
    ){}
}