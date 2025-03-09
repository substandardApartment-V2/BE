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
            Long maxFloorCount, Long basementFloorCount, Long passengerCargoElevatorCount, String buildingStructure,
            Boolean groundAccessibleToPublic, Boolean undergroundAccessibleToPublic,
            Long groundParkingSpaces, Long undergroundParkingSpaces,
            Long groundEvChargerCount, Long undergroundEvChargerCount, Long groundEvParkingSpaces,
            Long undergroundEvParkingSpaces, List<EvChargingFacilityDetail> evChargingFacilitiesDetails,
            String disinfectionManagementType, String disinfectionManagementContractor, Long annualDisinfectionFrequency,
            String securityManagementType, String securityManagementContractor, Long securityManagementStaff,
            String cleaningManagementType, String cleaningManagementContractor, String foodWasteDisposalMethod,
            Long generalManagementStaff
    ) {
        return new AptInfoDetail(
                maxFloorCount != null || basementFloorCount != null || passengerCargoElevatorCount != null || buildingStructure != null
                        ? new Building(maxFloorCount, basementFloorCount, passengerCargoElevatorCount, buildingStructure)
                        : null,
                groundAccessibleToPublic != null || undergroundAccessibleToPublic != null
                        ? new AccessibleToPublic(groundAccessibleToPublic, undergroundAccessibleToPublic)
                        : null,
                groundParkingSpaces != null || undergroundParkingSpaces != null
                        ? new Parking(groundParkingSpaces, undergroundParkingSpaces)
                        : null,
                groundEvChargerCount != null || undergroundEvChargerCount != null || groundEvParkingSpaces != null ||
                        undergroundEvParkingSpaces != null || (evChargingFacilitiesDetails != null && !evChargingFacilitiesDetails.isEmpty())
                        ? new EvCharging(groundEvChargerCount, undergroundEvChargerCount, groundEvParkingSpaces, undergroundEvParkingSpaces, evChargingFacilitiesDetails)
                        : null,
                disinfectionManagementType != null || disinfectionManagementContractor != null || annualDisinfectionFrequency != null
                        ? new DisinfectionManagement(disinfectionManagementType, disinfectionManagementContractor, annualDisinfectionFrequency)
                        : null,
                securityManagementType != null || securityManagementContractor != null || securityManagementStaff != null
                        ? new SecurityManagement(securityManagementType, securityManagementContractor, securityManagementStaff)
                        : null,
                cleaningManagementType != null || cleaningManagementContractor != null || foodWasteDisposalMethod != null
                        ? new CleaningManagement(cleaningManagementType, cleaningManagementContractor, foodWasteDisposalMethod)
                        : null,
                generalManagementStaff != null
                        ? new GeneralManagement(generalManagementStaff)
                        : null
        );
    }

    // 건물 정보를 나타내는 클래스입니다.
    public record Building(Long maxFloorCount, Long basementFloorCount, Long passengerCargoElevatorCount, String buildingStructure) {}

    // 외부인 개방 여부 정보를 나타내는 클래스입니다.
    public record AccessibleToPublic(Boolean groundAccessibleToPublic, Boolean undergroundAccessibleToPublic) {}

    // 주차 정보를 나타내는 클래스입니다.
    public record Parking(Long groundParkingSpaces, Long undergroundParkingSpaces) {}

    // 전기차 충전 정보를 나타내는 클래스입니다.
    public record EvCharging(Long groundEvChargerCount, Long undergroundEvChargerCount, Long groundEvParkingSpaces, Long undergroundEvParkingSpaces, List<EvChargingFacilityDetail> evChargingFacilitiesDetails) {}

    // 전기차 충전 시설 상세 정보를 나타내는 클래스입니다.
    public record EvChargingFacilityDetail(String location, String type, String connector, String chargingSpeed, Long count, String provider) {}

    // 소독 관리 정보를 나타내는 클래스입니다.
    public record DisinfectionManagement(String managementType, String contractor, Long annualFrequency) {}

    // 경비 관리 정보를 나타내는 클래스입니다.
    public record SecurityManagement(String managementType, String contractor, Long staffCount) {}

    // 청소 관리 정보를 나타내는 클래스입니다.
    public record CleaningManagement(String managementType, String contractor, String foodWasteDisposalMethod) {}

    // 일반 관리 정보를 나타내는 클래스입니다.
    public record GeneralManagement(Long staffCount) {}
}