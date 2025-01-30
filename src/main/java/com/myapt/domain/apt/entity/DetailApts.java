package com.myapt.domain.apt.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "DetailApts")
public class DetailApts {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY) 외부에서 id 입력 받음
    @Column(name = "detail_apts_id")
    private String id; //아파트 관리코드(단지코드)

    @Column(name = "complex_type")
    private String complexType; // 단지분류

    @Column(name = "postal_code")
    private String postalCode; // 우편번호

    @Column(name = "road_address")
    private String roadAddress; // 도로명 주소

    @Column(name = "sale_type")
    private String saleType; // 분양형태

    @Column(name = "approval_date")
    private String approvalDate; // 사용승인일

    @Column(name = "registration_date")
    private String registrationDate; // 등록일

    @Column(name = "number_of_buildings")
    private Long numberOfBuildings; // 동 수

    @Column(name = "number_of_units")
    private Long numberOfUnits; // 세대 수

    @Column(name = "number_of_sale_units")
    private Long numberOfSaleUnits; // 분양 세대 수

    @Column(name = "number_of_rent_units")
    private Long numberOfRentUnits; // 임대 세대 수

    @Column(name = "number_of_public_rent_units")
    private Long numberOfPublicRentUnits; // 공공임대 세대 수

    @Column(name = "number_of_private_rent_units")
    private Long numberOfPrivateRentUnits; // 민간임대 세대 수

    @Column(name = "management_type")
    private String managementType; // 관리 형태

    @Column(name = "heating_type")
    private String heatingType; // 난방 형태

    @Column(name = "corridor_type")
    private String corridorType; // 복도 형태

    @Column(name = "constructor")
    private String constructor; // 시공사

    @Column(name = "developer")
    private String developer; // 개발사

    @Column(name = "housing_manager")
    private String housingManager; // 주택 관리자

    @Column(name = "housing_manager_business_registration_number")
    private String housingManagerBusinessRegistrationNumber; // 주택 관리자 사업자등록번호

    @Column(name = "general_management_type")
    private String generalManagementType; // 일반 관리 형태

    @Column(name = "general_management_staff")
    private Long generalManagementStaff; // 일반 관리 인원

    @Column(name = "security_management_type")
    private String securityManagementType; // 경비 관리 형태

    @Column(name = "security_management_staff")
    private Long securityManagementStaff; // 경비 관리 인원

    @Column(name = "security_management_contractor")
    private String securityManagementContractor; // 경비 관리 용역

    @Column(name = "cleaning_management_type")
    private String cleaningManagementType; // 청소 관리 형태

    @Column(name = "cleaning_management_staff")
    private Long cleaningManagementStaff; // 청소 관리 인원

    @Column(name = "cleaning_management_contractor")
    private String cleaningManagementContractor; // 청소 관리 용역

    @Column(name = "food_waste_disposal_method")
    private String foodWasteDisposalMethod; // 음식물 쓰레기 처리 방법

    @Column(name = "disinfection_management_type")
    private String disinfectionManagementType; // 소독 관리 형태

    @Column(name = "disinfection_management_contractor")
    private String disinfectionManagementContractor; // 소독 관리 용역

    @Column(name = "annual_disinfection_frequency")
    private Long annualDisinfectionFrequency; // 연간 소독 횟수

    @Column(name = "disinfection_method")
    private String disinfectionMethod; // 소독 방법

    @Column(name = "building_structure")
    private String buildingStructure; // 건물 구조

    @Column(name = "electricity_supply_capacity")
    private Long electricitySupplyCapacity; // 전기 공급 용량

    @Column(name = "electricity_contract_type_per_unit")
    private String electricityContractTypePerUnit; // 세대별 전기 계약 유형

    @Column(name = "electricity_safety_manager_assigned")
    private Boolean electricitySafetyManagerAssigned; // 전기 안전 관리자 배정 여부

    @Column(name = "fire_receiver_type")
    private String fireReceiverType; // 화재 수신기 유형(화재수신반방식)

    @Column(name = "water_supply_method")
    private String waterSupplyMethod; // 급수 방식

    @Column(name = "elevator_management_type")
    private String elevatorManagementType; // 엘리베이터 관리 형태

    @Column(name = "passenger_elevator_count")
    private Long passengerElevatorCount; // 승용 엘리베이터 수

    @Column(name = "cargo_elevator_count")
    private Long cargoElevatorCount; // 화물 엘리베이터 수

    @Column(name = "passenger_cargo_elevator_count")
    private Long passengerCargoElevatorCount; // 승용/화물 겸용 엘리베이터 수

    @Column(name = "disabled_elevator_count")
    private Long disabledElevatorCount; // 장애인용 엘리베이터 수

    @Column(name = "emergency_elevator_count")
    private Long emergencyElevatorCount; // 비상용 엘리베이터 수

    @Column(name = "other_elevator_count")
    private Long otherElevatorCount; // 기타 엘리베이터 수

    @Column(name = "total_parking_spaces")
    private Long totalParkingSpaces; // 총 주차 공간 수

    @Column(name = "ground_parking_spaces")
    private Long groundParkingSpaces; // 지상 주차 공간 수

    @Column(name = "underground_parking_spaces")
    private Long undergroundParkingSpaces; // 지하 주차 공간 수

    @Column(name = "cctv_count")
    private Long cctvCount; // CCTV 수

    @Column(name = "home_network")
    private Boolean homeNetwork; // 홈 네트워크

    @Column(name = "emergency_vehicle_accessible_gate")
    private Boolean emergencyVehicleAccessibleGate; // 비상 차량 출입 가능 게이트

    @Column(name = "selected_gate_parking_control_system")
    private String selectedGateParkingControlSystem; // 선택 게이트 주차 제어 시스템

    @Column(name = "emergency_license_plate_recognition")
    private Boolean emergencyLicensePlateRecognition; // 비상 차량 번호판 인식

    @Column(name = "emergency_vehicle_access_method")
    private String emergencyVehicleAccessMethod; // 비상 차량 출입 방법

    @Column(name = "emergency_vehicle_instant_pass")
    private Boolean emergencyVehicleInstantPass; // 비상 차량 즉시 통과

    @Column(name = "management_office_address")
    private String managementOfficeAddress; // 관리 사무소 주소

    @Column(name = "management_office_contact")
    private String managementOfficeContact; // 관리 사무소 연락처

    @Column(name = "management_office_fax")
    private String managementOfficeFax; // 관리 사무소 팩스

    @Column(name = "amenities")
    private String amenities; // 편의 시설(부대복리시설)

    @Column(name = "max_floor_count")
    private Long maxFloorCount; // 최대 층 수(최고층수)

    @Column(name = "max_floor_count_building_register")
    private Long maxFloorCountBuildingRegister; // 건축물 등록 최대 층 수

    @Column(name = "basement_floor_count")
    private Long basementFloorCount; // 지하 층 수

    @Column(name = "total_vehicle_count")
    private Long totalVehicleCount; // 총 차량 수(차량보유대수-전체)

    @Column(name = "electric_vehicle_count")
    private Long electricVehicleCount; // 전기차 수(차량보유대수-전기차)

    @Column(name = "ground_ev_charger_installed")
    private Boolean groundEvChargerInstalled; // 지상 전기차 충전기 설치 여부

    @Column(name = "underground_ev_charger_installed")
    private Boolean undergroundEvChargerInstalled; // 지하 전기차 충전기 설치 여부

    @Column(name = "ground_ev_parking_spaces")
    private Long groundEvParkingSpaces; // 지상 전기차 주차 공간 수

    @Column(name = "underground_ev_parking_spaces")
    private Long undergroundEvParkingSpaces; // 지하 전기차 주차 공간 수

    @Column(name = "ground_accessible_to_public")
    private Boolean groundAccessibleToPublic; // 지상 공공 접근 가능 여부

    @Column(name = "underground_accessible_to_public")
    private Boolean undergroundAccessibleToPublic; // 지하 공공 접근 가능 여부

    @Column(name = "ground_access_start_time")
    private String groundAccessStartTime; // 지상 접근 시작 시간

    @Column(name = "underground_access_start_time")
    private String undergroundAccessStartTime; // 지하 접근 시작 시간

    @Column(name = "ground_access_end_time")
    private String groundAccessEndTime; // 지상 접근 종료 시간

    @Column(name = "underground_access_end_time")
    private String undergroundAccessEndTime; // 지하 접근 종료 시간

    @Column(name = "ev_charging_facilities_details")
    private String evChargingFacilitiesDetails; // 전기차 충전 시설 상세

    @Column(name = "ground_ev_charger_count")
    private Long groundEvChargerCount; // 지상 전기차 충전기 수

    @Column(name = "underground_ev_charger_count")
    private Long undergroundEvChargerCount; // 지하 전기차 충전기 수

    @Column(name = "resident_facilities")
    private String residentFacilities; // 주민 시설(입주편의시설)

    // 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apts_id")
    private Apts apts;

    //연관관계
    @OneToMany(mappedBy = "detailApts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MngCost> mngCosts;

}