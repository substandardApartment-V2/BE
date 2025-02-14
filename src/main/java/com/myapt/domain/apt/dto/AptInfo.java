package com.myapt.domain.apt.dto;

import lombok.Builder;

import java.util.List;

@Builder
// 아파트 기본 정보를 반환할 때 사용되는 DTO 클래스입니다.
public record AptInfo(
        AptBasicInfo aptInfo, // 아파트 기본 정보
        BuildInfo buildInfo, // 건물 기본 정보
        //List<SellingPrice> sellingPrice, // 매매가 정보
        MonthlyMaintenanceFees monthlyMaintenanceFees, // 월별 관리비
        EtcInfo etcInfo // 기타 정보
) {
    // of 메서드는 인자를 받아 AptInfo 객체를 생성하는데, 이는 DTO 객체를 생성하여 클라이언트로 반환할 때 유용합니다.
    @Builder
    public static AptInfo of(
            String detailId, String name, String roadAddress, String zipCode,
            String completionDate, String developer, String constructor, long numberOfUnits,
            //List<SellingPrice> sellingPrice,
            String year, List<MonthlyMaintenanceData> monthlyMaintenanceData,
            List<String> amenities, String buildingStructure, String managementType, String heatingType,
            long cctvCount, long totalParkingSpaces, long totalGroundEvChargerCount,
            String managementOfficeAddress, String managementOfficeContact, String managementOfficeFax,
            String housingManager
    ) {
        return new AptInfo(
                new AptBasicInfo(detailId, name, roadAddress, zipCode),
                new BuildInfo(completionDate, developer, constructor, numberOfUnits),
                //sellingPrice,
                new MonthlyMaintenanceFees(year, monthlyMaintenanceData),
                new EtcInfo(amenities, buildingStructure, managementType, heatingType, cctvCount, totalParkingSpaces,
                        totalGroundEvChargerCount, managementOfficeAddress, managementOfficeContact,
                        managementOfficeFax, housingManager)
        );
    }

    // 아파트 기본 정보를 나타내는 클래스입니다.
    public record AptBasicInfo(String detailId, String name, String roadAddress, String zipCode) {}

    // 건물 기본 정보를 나타내는 클래스입니다.
    public record BuildInfo(String completionDate, String developer, String constructor, long numberOfUnits) {}

    // 매매가 정보를 나타내는 클래스입니다.
    //public record SellingPrice(long netArea, long price) {}

    // 월별 관리비 정보를 나타내는 클래스입니다.
    public record MonthlyMaintenanceFees(String year, List<MonthlyMaintenanceData> data) {}

    // 월별 관리비 데이터 정보를 나타내는 클래스입니다.
    public record MonthlyMaintenanceData(String month, long fee) {}

    // 기타 정보를 나타내는 클래스입니다.
    public record EtcInfo(
            List<String> amenities, String buildingStructure, String managementType, String heatingType,
            long cctvCount, long totalParkingSpaces, long totalGroundEvChargerCount,
            String managementOfficeAddress, String managementOfficeContact, String managementOfficeFax,
            String housingManager
    ) {}
}
