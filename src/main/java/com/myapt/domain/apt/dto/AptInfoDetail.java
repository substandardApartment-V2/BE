package com.myapt.domain.apt.dto;

import lombok.Builder;

@Builder
public record AptInfoDetail( // 클라이언트에 아파트의 상세정보를 반환할 때 사용됩니다.
                             //건물
                             long maxFloorCount, // 최대 층 수 (최고층 수)
                             long basementFloorCount, // 지하 층 수
                             long passengerCargoElevatorCount, //승용&화물 엘레베이터 수
                             String buildingStructure, // 건물구조

                             //외부인 개방 여부
                             String approvalDate, // 사용승인일 (준공일)
                             String developer, // 시행사
                             String constructor, // 시공사
                             long numberOfUnits, // 세대수

                             //주차대수
                             long groundParkingSpaces, // 지상 주차 공간 수
                             long undergroundParkingSpaces, // 지하 주차 공간 수

                             //전기차
                             long groundEvChargerCount, // 지상 전기차 충전기 수
                             long undergroundEvChargerCount, // 지하 전기차 충전기 수
                             long groundEvParkingSpaces, // 지상 전기차 주차 공간 수
                             long undergroundEvParkingSpaces, // 지하 전기차 주차 공간 수
                             String evChargingFacilitiesDetails, // 전기차 충전 시설 상세

                             //소독관리
                             String disinfectionManagementType, // 소독 관리 형태
                             String disinfectionManagementContractor, // 소독 관리 용역
                             long annualDisinfectionFrequency, // 연간 소독 횟수

                             //경비관리
                             String securityManagementType, // 경비 관리 형태
                             String securityManagementContractor, // 경비 관리 용역
                             long securityManagementStaff, // 경비 관리 인원

                             //청소관리
                             String cleaningManagementType, // 청소 관리 형태
                             String cleaningManagementContractor, // 청소 관리 용역
                             String foodWasteDisposalMethod, // 음식물 쓰레기 처리 방법

                             //일반관리
                             long generalManagementStaff // 일반 관리 인원
)
{ // of 메서드는 인자를 받아 AptInfoDetail 객체를 생성하는데, 이는 DTO 객체를 생성하여 클라이언트로 반환할 때 유용합니다.
    public static AptInfoDetail of(
            long maxFloorCount, long basementFloorCount, long passengerCargoElevatorCount, String buildingStructure,
            String approvalDate, String developer, String constructor, long numberOfUnits,
            long groundParkingSpaces, long undergroundParkingSpaces,
            long groundEvChargerCount, long undergroundEvChargerCount, long groundEvParkingSpaces,
            long undergroundEvParkingSpaces, String evChargingFacilitiesDetails,
            String disinfectionManagementType, String disinfectionManagementContractor, long annualDisinfectionFrequency,
            String securityManagementType, String securityManagementContractor, long securityManagementStaff,
            String cleaningManagementType, String cleaningManagementContractor, String foodWasteDisposalMethod,
            long generalManagementStaff
    ) {
        return AptInfoDetail.builder()
                .maxFloorCount(maxFloorCount)
                .basementFloorCount(basementFloorCount)
                .passengerCargoElevatorCount(passengerCargoElevatorCount)
                .buildingStructure(buildingStructure)
                .approvalDate(approvalDate)
                .developer(developer)
                .constructor(constructor)
                .numberOfUnits(numberOfUnits)
                .groundParkingSpaces(groundParkingSpaces)
                .undergroundParkingSpaces(undergroundParkingSpaces)
                .groundEvChargerCount(groundEvChargerCount)
                .undergroundEvChargerCount(undergroundEvChargerCount)
                .groundEvParkingSpaces(groundEvParkingSpaces)
                .undergroundEvParkingSpaces(undergroundEvParkingSpaces)
                .evChargingFacilitiesDetails(evChargingFacilitiesDetails)
                .disinfectionManagementType(disinfectionManagementType)
                .disinfectionManagementContractor(disinfectionManagementContractor)
                .annualDisinfectionFrequency(annualDisinfectionFrequency)
                .securityManagementType(securityManagementType)
                .securityManagementContractor(securityManagementContractor)
                .securityManagementStaff(securityManagementStaff)
                .cleaningManagementType(cleaningManagementType)
                .cleaningManagementContractor(cleaningManagementContractor)
                .foodWasteDisposalMethod(foodWasteDisposalMethod)
                .generalManagementStaff(generalManagementStaff)
                .build();
    }
}

