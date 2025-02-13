package com.myapt.domain.apt.dto;

import lombok.Builder;
import java.util.List;
import java.util.Map;

@Builder
public record AptInfo(
        String detailId, // 아파트 상세 정보 ID
        String name, // 아파트 명
        String buildingType, // 건물 종류
        String roadAddress, // 도로명 주소
        String zipCode, // 우편 번호
        String completionDate, // 준공일
        String developer, // 시행사
        String constructor, // 시공사
        long numberOfUnits, // 세대수
        // List<SalePriceInfo> salePrices, // 면적별 매매가
        Map<String, Long> monthlyMaintenanceFees, // 월별 관리비
        List<String> amenities, // 부대복리시설을 List로 변경
        String buildingStructure, // 건물구조
        String managementType, // 관리방식
        String heatingType, // 난방방식
        long cctvCount, // CCTV 대수(대)
        long totalParkingSpaces, // 총주차 대수(대)
        long totalGroundEvChargerCount, // 총 전기차 충전기 대수
        String managementOfficeAddress, // 관리사무소 주소
        String managementOfficeContact, // 관리사무소 연락처
        String managementOfficeFax, // 관리사무소 팩스
        String housingManager // 주택관리업자
) {
    @Builder
    public static record SalePriceInfo(long area, long price) {
        // SalePriceInfo 레코드는 AptInfo의 속성 중 하나인 salePrices를 구성하는 요소입니다. 이 레코드는 각 아파트의 면적과 해당 면적의 매매가를 표현하기 위해 사용됩니다.
    }

    // of 메서드는 인자를 받아 ApartmentDetailInfo 객체를 생성하는데, 이는 DTO 객체를 생성하여 클라이언트로 반환할 때 유용합니다.
    public static AptInfo of(
            String detailId, String name, String buildingType, String roadAddress, String zipCode, String completionDate,
            String developer, String constructor, long numberOfUnits,
            // List<SalePriceInfo> salePrices,
            Map<String, Long> monthlyMaintenanceFees,
            List<String> amenities, // Map에서 List로 변경
            String buildingStructure, String managementType, String heatingType, long cctvCount,
            long totalParkingSpaces, long totalGroundEvChargerCount, // 총 전기차 충전기 대수 추가
            String managementOfficeAddress, String managementOfficeContact,
            String managementOfficeFax, String housingManager
    ) {
        return AptInfo.builder()
                .detailId(detailId)
                .name(name)
                .buildingType(buildingType)
                .roadAddress(roadAddress)
                .zipCode(zipCode)
                .completionDate(completionDate)
                .developer(developer)
                .constructor(constructor)
                .numberOfUnits(numberOfUnits)
                //.salePrices(salePrices)
                .monthlyMaintenanceFees(monthlyMaintenanceFees)
                .amenities(amenities) // List로 된 amenities를 사용
                .buildingStructure(buildingStructure)
                .managementType(managementType)
                .heatingType(heatingType)
                .cctvCount(cctvCount)
                .totalParkingSpaces(totalParkingSpaces)
                .totalGroundEvChargerCount(totalGroundEvChargerCount) // 총 전기차 충전기 대수 빌더에 추가
                .managementOfficeAddress(managementOfficeAddress)
                .managementOfficeContact(managementOfficeContact)
                .managementOfficeFax(managementOfficeFax)
                .housingManager(housingManager)
                .build();
    }
}
