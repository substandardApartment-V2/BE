package com.myapt.domain.apt.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.util.List;

@JsonPropertyOrder({// JSON 출력 시, 아래의 명시된 순서대로 필드를 반환합니다.
        "building",                // 건물 정보
        "accessibleToPublic",      // 외부인 개방 여부 정보
        "parking",                 // 주차 정보
        "evCharging",              // 전기차 충전 정보
        "disinfectionManagement",  // 소독 관리 정보
        "securityManagement",      // 경비 관리 정보
        "cleaningManagement",      // 청소 관리 정보
        "generalManagement"        // 일반 관리 정보
})
// 아파트의 상세정보를 반환할 때 사용되는 DTO 클래스입니다.
public record AptInfoDetail(
    Building building, // 건물 정보
    AccessibleToPublic accessibleToPublic, // 외부인 개방 여부 정보
    Parking parking, // 주차 정보
    EvCharging evCharging, // 전기차 충전 정보
    DisinfectionManagement disinfectionManagement, // 소독 관리 정보
    SecurityManagement securityManagement, // 경비 관리 정보
    CleaningManagement cleaningManagement, // 청소 관리 정보
    GeneralManagement generalManagement // 일반 관리 정보
) {
    // of 메서드는 인자를 받아 AptInfoDetail 객체를 생성하는데, 이는 DTO 객체를 생성하여 클라이언트로 반환할 때 유용합니다.
    @Builder
    public static AptInfoDetail of(
        long maxFloorCount, long basementFloorCount, long passengerCargoElevatorCount, String buildingStructure,
        boolean groundAccessibleToPublic, boolean undergroundAccessibleToPublic,
        long groundParkingSpaces, long undergroundParkingSpaces,
        long groundEvChargerCount, long undergroundEvChargerCount, long groundEvParkingSpaces,
        long undergroundEvParkingSpaces, List<EvChargingFacilityDetail> evChargingFacilitiesDetails,
        String disinfectionManagementType, String disinfectionManagementContractor, long annualDisinfectionFrequency,
        String securityManagementType, String securityManagementContractor, long securityManagementStaff,
        String cleaningManagementType, String cleaningManagementContractor, String foodWasteDisposalMethod,
        long generalManagementStaff
    ) {
        return new AptInfoDetail(
            new Building(maxFloorCount, basementFloorCount, passengerCargoElevatorCount, buildingStructure),
            new AccessibleToPublic(groundAccessibleToPublic, undergroundAccessibleToPublic),
            new Parking(groundParkingSpaces, undergroundParkingSpaces),
            new EvCharging(groundEvChargerCount, undergroundEvChargerCount, groundEvParkingSpaces, undergroundEvParkingSpaces, evChargingFacilitiesDetails),
            new DisinfectionManagement(disinfectionManagementType, disinfectionManagementContractor, annualDisinfectionFrequency),
            new SecurityManagement(securityManagementType, securityManagementContractor, securityManagementStaff),
            new CleaningManagement(cleaningManagementType, cleaningManagementContractor, foodWasteDisposalMethod),
            new GeneralManagement(generalManagementStaff)
        );
    }

    // 건물 정보를 나타내는 클래스입니다.
    public record Building(long maxFloorCount, long basementFloorCount, long passengerCargoElevatorCount, String buildingStructure) {}

    // 외부인 개방 여부 정보를 나타내는 클래스입니다.
    public record AccessibleToPublic(boolean groundAccessibleToPublic, boolean undergroundAccessibleToPublic) {}

    // 주차 정보를 나타내는 클래스입니다.
    public record Parking(long groundParkingSpaces, long undergroundParkingSpaces) {}

    // 전기차 충전 정보를 나타내는 클래스입니다.
    public record EvCharging(long groundEvChargerCount, long undergroundEvChargerCount, long groundEvParkingSpaces, long undergroundEvParkingSpaces, List<EvChargingFacilityDetail> evChargingFacilitiesDetails) {}

    // 전기차 충전 시설 상세 정보를 나타내는 클래스입니다.
    public record EvChargingFacilityDetail(String location, String type, String connector, String chargingSpeed, long count, String provider) {}

    // 소독 관리 정보를 나타내는 클래스입니다.
    public record DisinfectionManagement(String managementType, String contractor, long annualFrequency) {}

    // 경비 관리 정보를 나타내는 클래스입니다.
    public record SecurityManagement(String managementType, String contractor, long staffCount) {}

    // 청소 관리 정보를 나타내는 클래스입니다.
    public record CleaningManagement(String managementType, String contractor, String foodWasteDisposalMethod) {}

    // 일반 관리 정보를 나타내는 클래스입니다.
    public record GeneralManagement(long staffCount) {}
}