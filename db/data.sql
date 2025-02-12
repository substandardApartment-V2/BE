-- 테이블 생성
-- 아파트 테이블
create table apts
(
    apts_id             varchar(255) not null
        primary key,
    apt_nm              varchar(255) not null,
    buld_stru           varchar(255) null,
    cnst_entrprs_nm     varchar(255) null,
    hus_mngm_entrprs_nm varchar(255) null,
    la                  double       not null,
    lo                  double       not null,
    nmhsh               int          null,
    rdnmadr             varchar(255) not null,
    use_aprv_year       int          null
) ;


-- 부실 아파트 테이블
create table defect_apts
(
    defect_apts_id bigint auto_increment
        primary key,
    defect_type    varchar(255) not null,
    desgnr         varchar(255) not null,
    reinf_method   varchar(255) not null,
    reinf_status   varchar(255) not null,
    sprvsr         varchar(255) not null,
    zipcode        varchar(255) not null,
    apts_id        varchar(255) null,
    constraint FK8a4knpfwna8rdnimwn9mu9d4a
        foreign key (apts_id) references apts (apts_id)
) ;

-- 아파트 상세 테이블
create table detail_apts
(
    detail_apts_id                               varchar(255) not null
        primary key,
    amenities                                    varchar(255) null,
    annual_disinfection_frequency                bigint       null,
    approval_date                                varchar(255) null,
    basement_floor_count                         bigint       null,
    building_structure                           varchar(255) null,
    cargo_elevator_count                         bigint       null,
    cctv_count                                   bigint       null,
    cleaning_management_contractor               varchar(255) null,
    cleaning_management_staff                    bigint       null,
    cleaning_management_type                     varchar(255) null,
    complex_type                                 varchar(255) null,
    constructor                                  varchar(255) null,
    corridor_type                                varchar(255) null,
    developer                                    varchar(255) null,
    disabled_elevator_count                      bigint       null,
    disinfection_management_contractor           varchar(255) null,
    disinfection_management_type                 varchar(255) null,
    disinfection_method                          varchar(255) null,
    electric_vehicle_count                       bigint       null,
    electricity_contract_type_per_unit           varchar(255) null,
    electricity_safety_manager_assigned          bit          null,
    electricity_supply_capacity                  bigint       null,
    elevator_management_type                     varchar(255) null,
    emergency_elevator_count                     bigint       null,
    emergency_license_plate_recognition          bit          null,
    emergency_vehicle_access_method              varchar(255) null,
    emergency_vehicle_accessible_gate            bit          null,
    emergency_vehicle_instant_pass               bit          null,
    ev_charging_facilities_details               varchar(255) null,
    fire_receiver_type                           varchar(255) null,
    food_waste_disposal_method                   varchar(255) null,
    general_management_staff                     bigint       null,
    general_management_type                      varchar(255) null,
    ground_access_end_time                       varchar(255) null,
    ground_access_start_time                     varchar(255) null,
    ground_accessible_to_public                  bit          null,
    ground_ev_charger_count                      bigint       null,
    ground_ev_charger_installed                  bit          null,
    ground_ev_parking_spaces                     bigint       null,
    ground_parking_spaces                        bigint       null,
    heating_type                                 varchar(255) null,
    home_network                                 bit          null,
    housing_manager                              varchar(255) null,
    housing_manager_business_registration_number varchar(255) null,
    management_office_address                    varchar(255) null,
    management_office_contact                    varchar(255) null,
    management_office_fax                        varchar(255) null,
    management_type                              varchar(255) null,
    max_floor_count                              bigint       null,
    max_floor_count_building_register            bigint       null,
    number_of_buildings                          bigint       null,
    number_of_private_rent_units                 bigint       null,
    number_of_public_rent_units                  bigint       null,
    number_of_rent_units                         bigint       null,
    number_of_sale_units                         bigint       null,
    number_of_units                              bigint       null,
    other_elevator_count                         bigint       null,
    passenger_cargo_elevator_count               bigint       null,
    passenger_elevator_count                     bigint       null,
    postal_code                                  varchar(255) null,
    registration_date                            varchar(255) null,
    resident_facilities                          varchar(255) null,
    road_address                                 varchar(255) null,
    sale_type                                    varchar(255) null,
    security_management_contractor               varchar(255) null,
    security_management_staff                    bigint       null,
    security_management_type                     varchar(255) null,
    selected_gate_parking_control_system         varchar(255) null,
    total_parking_spaces                         bigint       null,
    total_vehicle_count                          bigint       null,
    underground_access_end_time                  varchar(255) null,
    underground_access_start_time                varchar(255) null,
    underground_accessible_to_public             bit          null,
    underground_ev_charger_count                 bigint       null,
    underground_ev_charger_installed             bit          null,
    underground_ev_parking_spaces                bigint       null,
    underground_parking_spaces                   bigint       null,
    water_supply_method                          varchar(255) null,
    apts_id                                      varchar(255) null,
    constraint FK896syuqrqc6njrcpsy7kdiksn
        foreign key (apts_id) references apts (apts_id)
) ;

-- 관리비 상세 테이블
create table mng_cost
(
    mngcost_id                          varchar(255) not null
        primary key,
    association_cost                    bigint       null,
    building_insurance_fee              bigint       null,
    city_county_district                varchar(255) null,
    cleaning_cost                       bigint       null,
    clothing_cost                       bigint       null,
    common_contribution_revenue         bigint       null,
    complex_name                        varchar(255) null,
    disaster_prevention_cost            bigint       null,
    disinfection_cost                   bigint       null,
    election_cost                       bigint       null,
    electricity_cost_common             bigint       null,
    electricity_cost_individual         bigint       null,
    elevator_maintenance_cost           bigint       null,
    etc                                 bigint       null,
    facility_maintenance_cost           bigint       null,
    gas_usage_cost_common               bigint       null,
    gas_usage_cost_individual           bigint       null,
    heating_cost_common                 bigint       null,
    heating_cost_individual             bigint       null,
    hot_water_cost_common               bigint       null,
    hot_water_cost_individual           bigint       null,
    individual_usage_sum                bigint       not null,
    intelligent_network_maintenance     bigint       null,
    labor_cost                          bigint       null,
    management_commission_fee           bigint       null,
    miscellaneous_income_monthly_amount bigint       null,
    occurrence_year_month               bigint       null,
    office_expenses                     bigint       null,
    other_incidental_expenses           bigint       null,
    province                            varchar(255) null,
    repair_cost                         bigint       null,
    reserve_fund_accumulation_rate      bigint       null,
    reserve_fund_monthly_charge         bigint       null,
    reserve_fund_monthly_expenditure    bigint       null,
    reserve_fund_total_accumulated      bigint       null,
    resident_contribution_revenue       bigint       null,
    safety_inspection_cost              bigint       null,
    security_cost                       bigint       null,
    sewage_fee                          bigint       null,
    street                              varchar(255) null,
    taxes_and_dues                      bigint       null,
    total_common_management_fee_sum     bigint       not null,
    town_village                        varchar(255) null,
    training_cost                       bigint       null,
    tvfee                               bigint       null,
    vehicle_maintenance_cost            bigint       null,
    waste_fee                           bigint       null,
    water_cost_common                   bigint       null,
    water_cost_individual               bigint       null,
    detail_apts_id                      varchar(255) null,
    constraint FKdgyt3i6et1gw710nrjr7nb40l
        foreign key (detail_apts_id) references detail_apts (detail_apts_id)
) ;

-- 뉴스 테이블
create table news
(
    id         bigint auto_increment
        primary key,
    content    varchar(255) null,
    created_at datetime(6)  not null,
    image      varchar(255) null,
    platform   varchar(255) null,
    title      varchar(255) null,
    type       varchar(255) null,
    updated_at datetime(6)  not null,
    url        varchar(255) null
) ;

-- 공지사항 테이블
create table notices
(
    notices_id bigint auto_increment
        primary key,
    content    varchar(255) not null,
    created_at datetime(6)  not null,
    title      varchar(255) not null,
    updated_at datetime(6)  not null
) ;

-- 사용자 더미 데이터
-- 아파트 기본정보
INSERT INTO my_apt.apts (apts_id, apt_nm, rdnmadr, nmhsh, use_aprv_year, lo, la, cnst_entrprs_nm, hus_mngm_entrprs_nm, buld_stru) VALUES
    ('APT1111030000080230000001', '경희궁 롯데캐슬아파트', '서울특별시 종로구 통일로 230', 195, 2019, 126.959518033, 37.5737761339, '롯데건설(주)', '세화종합관리(주)', '철근콘크리트구조');
INSERT INTO my_apt.apts (apts_id, apt_nm, rdnmadr, nmhsh, use_aprv_year, lo, la, cnst_entrprs_nm, hus_mngm_entrprs_nm, buld_stru) VALUES
    ('APT01', '초롱꽃마을LH3단지', '경기도 파주시 초롱꽃로 17', 1663, 2022, 37.71419573, 126.7085488, '대보건설', '삼성', '무량판');

-- 부실 아파트
INSERT INTO my_apt.defect_apts (defect_apts_id, apts_id, defect_type, desgnr, reinf_method, reinf_status, sprvsr, zipcode) VALUES
    (1, 'APT01', '구조계산 미반영', '에스아이/에이유,한림구조', '보완 완료', '슬래브 보완 완료', '건원,신화,한빛', '12345');


-- 아파트 상세정보
INSERT INTO my_apt.detail_apts (
    detail_apts_id, complex_type, postal_code, road_address, sale_type, approval_date, registration_date,
    number_of_buildings, number_of_units, number_of_sale_units, number_of_rent_units, number_of_public_rent_units,
    number_of_private_rent_units, management_type, heating_type, corridor_type, constructor, developer, housing_manager,
    housing_manager_business_registration_number, general_management_type, general_management_staff,
    security_management_type, security_management_staff, security_management_contractor, cleaning_management_type,
    cleaning_management_staff, cleaning_management_contractor, food_waste_disposal_method, disinfection_management_type,
    disinfection_management_contractor, annual_disinfection_frequency, disinfection_method, building_structure,
    electricity_supply_capacity, electricity_contract_type_per_unit, electricity_safety_manager_assigned,
    fire_receiver_type, water_supply_method, elevator_management_type, passenger_elevator_count, cargo_elevator_count,
    passenger_cargo_elevator_count, disabled_elevator_count, emergency_elevator_count, other_elevator_count,
    total_parking_spaces, ground_parking_spaces, underground_parking_spaces, cctv_count, home_network,
    emergency_vehicle_accessible_gate, selected_gate_parking_control_system, emergency_license_plate_recognition,
    emergency_vehicle_access_method, emergency_vehicle_instant_pass, management_office_address, management_office_contact,
    management_office_fax, amenities, max_floor_count, max_floor_count_building_register, basement_floor_count,
    total_vehicle_count, electric_vehicle_count, ground_ev_charger_installed, underground_ev_charger_installed,
    ground_ev_parking_spaces, underground_ev_parking_spaces, ground_accessible_to_public, underground_accessible_to_public,
    ground_access_start_time, underground_access_start_time, ground_access_end_time, underground_access_end_time,
    ev_charging_facilities_details, ground_ev_charger_count, underground_ev_charger_count, resident_facilities, apts_id
) VALUES (
             'A10025710', 'Residential', '12345', '123 Main St, City', 'Sale', '2023-01-01', '2023-02-01',
             5, 100, 80, 20, 10,
             10, 'Central', 'Gas', 'Closed', 'ABC Construction', 'XYZ Developers', 'John Doe',
             '123-45-67890', 'In-house', 10,
             'Contracted', 5, 'Security Inc.', 'In-house',
             5, 'Cleaning Co.', 'Composting', 'Contracted',
             'Disinfection Services', 4, 'Chemical', 'Concrete',
             1000, 'Individual', TRUE,
             'Type A', 'Centralized', 'Contracted', 2, 1,
             1, 0, 0, 0,
             200, 100, 100, 50, TRUE,
             TRUE, 'Automated', TRUE,
             'Manual', TRUE, '123 Main Office St', '123-456-7890',
             '123-456-7891', '관리사무소, 노인정, 주민공동시설, 어린이놀이터, 휴게시설, 커뮤니티공간, 자전거보관소', 15, 15, 3,
             150, 10, TRUE, TRUE,
             5, 5, TRUE, TRUE,
             '06:00', '06:00', '22:00', '22:00',
             '◆1◆|지하|스탠드형충전기|AC단상 5핀|완속|2|kepco|,◆2◆|지하|스탠드형충전기|AC3상 7핀|급속|1|kepco|', 2, 2, 'Community Hall', 'APT1111030000080230000001');


-- 관리비 상세정보
INSERT INTO my_apt.mng_cost (
    mngcost_id,
    association_cost,
    building_insurance_fee,
    city_county_district,
    cleaning_cost,
    clothing_cost,
    common_contribution_revenue,
    complex_name,
    disaster_prevention_cost,
    disinfection_cost,
    election_cost,
    electricity_cost_common,
    electricity_cost_individual,
    elevator_maintenance_cost,
    etc,
    facility_maintenance_cost,
    gas_usage_cost_common,
    gas_usage_cost_individual,
    heating_cost_common,
    heating_cost_individual,
    hot_water_cost_common,
    hot_water_cost_individual,
    individual_usage_sum,
    intelligent_network_maintenance,
    labor_cost,
    management_commission_fee,
    miscellaneous_income_monthly_amount,
    occurrence_year_month,
    office_expenses,
    other_incidental_expenses,
    province,
    repair_cost,
    reserve_fund_accumulation_rate,
    reserve_fund_monthly_charge,
    reserve_fund_monthly_expenditure,
    reserve_fund_total_accumulated,
    resident_contribution_revenue,
    safety_inspection_cost,
    security_cost,
    sewage_fee,
    street,
    taxes_and_dues,
    total_common_management_fee_sum,
    town_village,
    training_cost,
    tvfee,
    vehicle_maintenance_cost,
    waste_fee,
    water_cost_common,
    water_cost_individual,
    detail_apts_id
) VALUES ('ID2', 51000, 21000, 'District2', 16000, 11000, 21000, 'ComplexB', 11000,
          5100, 3100, 8100, 10100, 15100, 3100, 12100, 5100, 3100, 2100, 1510,
          3100, 1010, 24573260, 7100, 20100, 15100, 5100, 202402, 8100, 2010,
          'Province2', 15100, 5, 2725150, 201000, 101000, 25100, 5100, 15100,
          5100, 'Street2', 3100, 33060740, 'Village2', 2010, 5100, 3100,
          510, 6100, 1010, 'A10025710');


-- 공지사항 더미 데이터
INSERT INTO my_apt.notices (created_at, notices_id, updated_at, content, title) VALUES ('2025-02-08 11:05:59.000000', 1, '2025-02-08 11:06:01.000000', '아파트 정보 조회 서비스가 런칭되었습니다.\\n많은 이용 부탁드립니다. 감사합니다.', '아파트 정보 조회 서비스 첫 서비스 시작..!(v1.0.0)');
INSERT INTO my_apt.notices (created_at, notices_id, updated_at, content, title) VALUES ('2025-02-10 11:07:36.000000', 2, '2025-02-10 11:07:38.000000', '뉴스 데이터 및 아파트 상세 정보 부분 버그가 수정되었습니다.', '서비스 업데이트(v1.0.1)');
INSERT INTO my_apt.notices (created_at, notices_id, updated_at, content, title) VALUES ('2025-02-12 11:08:43.000000', 3, '2025-02-12 11:08:44.000000', '현재 뉴스 데이터가 일반 아파트 정보와 부실 아파트 정보가 구분되지 않는 버그가 발생하였습니다. 조속히 해결 하도록 하겠습니다.\\n 감사합니다.', '버그 안내');
INSERT INTO my_apt.notices (created_at, notices_id, updated_at, content, title) VALUES ('2025-02-12 11:09:22.000000', 4, '2025-02-12 11:09:23.000000', '현재 사이트에 트래픽이 많이 발생함으로 불편을 야기할 수도 있습니다.', '트래픽 안내');
