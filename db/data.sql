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
    use_aprv_year       int          null,
    is_defect           bit          not null
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
INSERT INTO my_apt.apts (apts_id, apt_nm, rdnmadr, nmhsh, use_aprv_year, lo, la, cnst_entrprs_nm, hus_mngm_entrprs_nm, buld_stru, is_defect) VALUES ('APT1111030000080230000001', '경희궁 롯데캐슬아파트', '철근콘크리트구조', 195, 2019, 126.959518033, 37.5737761339, '롯데건설', '세화종합관리(주)', '철근콘크리트', false);
INSERT INTO my_apt.apts (apts_id, apt_nm, rdnmadr, nmhsh, use_aprv_year, lo, la, cnst_entrprs_nm, hus_mngm_entrprs_nm, buld_stru, is_defect) VALUES ('APT1168048624120011000001', '디아크리온강남', '서울특별시 강남구 자곡로 11길 11', 597, 2023, 127.1071361532, 37.4767934538, '양우종합건설', null, '무량판', true);
INSERT INTO my_apt.apts (apts_id, apt_nm, rdnmadr, nmhsh, use_aprv_year, lo, la, cnst_entrprs_nm, hus_mngm_entrprs_nm, buld_stru, is_defect) VALUES ('APT3315032510130550003901', '공주월송엘에이치천년나무4단지', '충청남도 공주시 무령로 550-39', 374, 2022, 127.1514146184, 36.4723878921, null, null, '무량판', true);
INSERT INTO my_apt.apts (apts_id, apt_nm, rdnmadr, nmhsh, use_aprv_year, lo, la, cnst_entrprs_nm, hus_mngm_entrprs_nm, buld_stru, is_defect) VALUES ('APT4148032061370050000001', '후곡마을뜨란채주공4단지', '경기도 파주시 후곡로 50', 1638, 2004, 126.771139829, 37.7532464307, '대한주택공사', '대원종합관리(주)', '철근콘크리트', false);

-- 부실 아파트
INSERT INTO my_apt.defect_apts (defect_apts_id, defect_type, desgnr, reinf_method, reinf_status, sprvsr, zipcode, apts_id) VALUES (1, '도면표현 미흡', '숨비/노드플랜,광장구조', '보완 완료', '기둥신설, 슬래브보완완료', '직접감독', '12345', 'APT1168048624120011000001');
INSERT INTO my_apt.defect_apts (defect_apts_id, defect_type, desgnr, reinf_method, reinf_status, sprvsr, zipcode, apts_id) VALUES (2, '배근도 인해 및 도면검토 부족', '엠디에이,태경구조', '보완 완료', '슬래브보완 완료', '직접감독', '12345', 'APT3315032510130550003901');


-- 아파트 상세정보
INSERT INTO detail_apts (detail_apts_id, electricity_safety_manager_assigned, emergency_license_plate_recognition, emergency_vehicle_accessible_gate, emergency_vehicle_instant_pass, ground_accessible_to_public, ground_ev_charger_installed, home_network, underground_accessible_to_public, underground_ev_charger_installed, annual_disinfection_frequency, basement_floor_count, cargo_elevator_count, cctv_count, cleaning_management_staff, disabled_elevator_count, electric_vehicle_count, electricity_supply_capacity, emergency_elevator_count, general_management_staff, ground_ev_charger_count, ground_ev_parking_spaces, ground_parking_spaces, max_floor_count, max_floor_count_building_register, number_of_buildings, number_of_private_rent_units, number_of_public_rent_units, number_of_rent_units, number_of_sale_units, number_of_units, other_elevator_count, passenger_cargo_elevator_count, passenger_elevator_count, security_management_staff, total_parking_spaces, total_vehicle_count, underground_ev_charger_count, underground_ev_parking_spaces, underground_parking_spaces, amenities, approval_date, building_structure, cleaning_management_contractor, cleaning_management_type, complex_type, constructor, corridor_type, developer, disinfection_management_contractor, disinfection_management_type, disinfection_method, electricity_contract_type_per_unit, elevator_management_type, emergency_vehicle_access_method, ev_charging_facilities_details, fire_receiver_type, food_waste_disposal_method, general_management_type, ground_access_end_time, ground_access_start_time, heating_type, housing_manager, housing_manager_business_registration_number, management_office_address, management_office_contact, management_office_fax, management_type, postal_code, registration_date, resident_facilities, road_address, sale_type, security_management_contractor, security_management_type, selected_gate_parking_control_system, underground_access_end_time, underground_access_start_time, water_supply_method, apts_id) VALUES
('A10023132', true, true, true, true, true, false, true, true, true, 4, 2, 0, 320, 6, 0, 32, 2350, 0, 6, 0, 0, 4, 15, 15, 8, 0, 199, 199, 398, 597, 0, 0, 16, 4, 723, 776, 37, 37, 719, '관리사무소, 보육시설, 문고, 주민공동시설, 어린이놀이터, 휴게시설, 커뮤니티공간, 자전거보관소', '20230613', '철근콘크리트구조', '(주)홈스웰', '위탁관리', '', '양우건설', '혼합식', '대한토지주택공사', '(주)한국종합방제', '위탁관리', '분무식', '단일계약', '위탁관리', '여', '◆1◆|지하|벽부형충전기|AC단상 5핀|완속|36|클린일렉스|,◆2◆|지하|스탠드형충전기|DC콤보 7핀|급속|1|클린일렉스|', 'GR형', '음식물쓰레기종량제', '위탁관리', '0시 0분', '0시 0분', '지역난방', '우리관리(주)', '1148123107', '서울 강남구 자곡로11길 11 (디아크리온강남)', '024452041', '024452043', '위탁관리', '06369', '20230710', '있음', '서울특별시 강남구 자곡로11길 11', '혼합', '(주)홈스웰', '위탁관리', '정문', '23시 59분', '시 분', '부스타방식', 'APT1168048624120011000001'),
('A41370508', true, false, true, true, true, true, false, true, true, 12, 1, 0, 205, 12, 0, 5, 3600, 0, 10, 38, 38, 1020, 23, 23, 21, 0, 0, 0, 1638, 1638, 0, 0, 46, 14, 1679, 5, 4, 4, 659, '관리사무소, 노인정, 보육시설, 주민공동시설, 어린이놀이터, 자전거보관소', '20041115', '철근콘크리트구조', '청아시스템', '위탁관리', '아파트', '대한토지주택공사', '계단식', '대한토지주택공사', '주신환경', '위탁관리', '도포식,분무식', '단일계약', '위탁관리', '가능', '◆1◆|지하|스탠드형충전기|AC단상 5핀|급속|2|한국전력|,◆2◆|지하|스탠드형충전기|DC차데모 10핀|완속|2|한국전력|,◆3◆|지상|스탠드형충전기|AC3상 7핀|완속|38|에버온|', 'R형', '음식물쓰레기종량제', '위탁관리', '23시 59분', '23시 59분', '개별난방', '대원종합관리(주)', '1178106882', '경기도 파주시 후곡로 50 후곡마을4단지 관리사무소', '0319414157', '0319414159', '위탁관리', '10920', '20090728', '없음', '경기도 파주시 후곡로 50', '분양', '대한종합관리(주)', '위탁관리', '정문', '23시 58분', '23시 59분', '부스타방식', 'APT4148032061370050000001');


-- 관리비 상세정보
INSERT INTO mng_cost (mngcost_id, province, city_county_district, town_village, street, complex_name, occurrence_year_month, individual_usage_sum, heating_cost_common, heating_cost_individual, hot_water_cost_common, hot_water_cost_individual, gas_usage_cost_common, gas_usage_cost_individual, electricity_cost_common, electricity_cost_individual, water_cost_common, water_cost_individual, tvfee, sewage_fee, waste_fee, association_cost, building_insurance_fee, election_cost, etc, reserve_fund_monthly_charge, reserve_fund_monthly_expenditure, reserve_fund_total_accumulated, reserve_fund_accumulation_rate, miscellaneous_income_monthly_amount, resident_contribution_revenue, common_contribution_revenue, total_common_management_fee_sum, labor_cost, office_expenses, taxes_and_dues, clothing_cost, training_cost, vehicle_maintenance_cost, other_incidental_expenses, cleaning_cost, security_cost, disinfection_cost, elevator_maintenance_cost, intelligent_network_maintenance, repair_cost, facility_maintenance_cost, safety_inspection_cost, disaster_prevention_cost, management_commission_fee, detail_apts_id) VALUES
('1', '서울특별시', '강남구', '', '자곡동', '디아크리온강남 아파트', 202401, 60456168, 26334870, 1187981, 369770, 234300, 190000, 0, 919679, 13916980, 14799360, 0, 1760000, 0, 0, 743228, 0, 0, 0, 111925736, 1369348, 29646750, 0, 22154860, 0, 736480, 16231360, 28490410, 0, 9702190, 0, 0, 736480, 1824900, 782955, 250000, 0, 0, 0, 0, 0, 173997, 0, 173997, 'A10023132'),
('2', '서울특별시', '강남구', '', '자곡동', '디아크리온강남 아파트', 202402, 62432118, 28245060, 985731, 237490, 0, 22000, 0, 497529, 14391300, 14689880, 410000, 1760000, 0, 449900, 743228, 0, 0, 0, 97561288, 1422460, 19576200, 0, 17283770, 0, 658160, 23250560, 23532640, 0, 8613080, 0, 0, 658160, 1433300, 782955, 350000, 0, 0, 0, 0, 0, 18070900, 10076000, 7994900, 'A10023132'),
('3', '서울특별시', '강남구', '', '자곡동', '디아크리온강남 아파트', 202403, 61462918, 26377010, 2195531, 177250, 0, 0, 0, 634999, 14274240, 14799360, 410000, 1760000, 0, 91300, 743228, 0, 0, 0, 91356096, 1422460, 13477800, 0, 19677990, 0, 678470, 10399631, 32650970, 0, 9772350, 0, 0, 678470, 1122600, 859355, 616000, 0, 0, 0, 0, 0, 17424292, 6000000, 11424292, 'A10023132'),
('4', '서울특별시', '강남구', '', '자곡동', '디아크리온강남 아파트', 202404, 59914334, 25986136, 1116131, 208330, 249340, 0, 0, 521379, 14409550, 14799360, 410000, 880000, 0, 590880, 743228, 0, 0, 0, 63206332, 1422460, 1453200, 0, 14866040, 0, 683630, 7561436, 24822570, 0, 9363810, 0, 0, 683630, 1416600, 782955, 150000, 0, 0, 0, 0, 0, 9875384, 0, 9875384, 'A10023132'),
('5', '서울특별시', '강남구', '', '자곡동', '디아크리온강남 아파트', 202405, 60443608, 26365810, 807864, 184800, 249340, 35000, 0, 675756, 14175900, 15158130, 410000, 880000, 0, 590880, 910128, 0, 0, 0, 59601376, 1422460, 207900, 0, 11274320, 0, 756870, 7502634, 25764960, 0, 9704330, 0, 0, 756870, 1428060, 782970, 0, 0, 0, 0, 0, 0, 45251176, 7956000, 37295176, 'A10023132'),
('6', '서울특별시', '강남구', '', '자곡동', '디아크리온강남 아파트', 202406, 61660873, 26010610, 901544, 224980, 372520, 0, 0, 732256, 14136850, 14852687, 410000, 1760000, 0, 1347220, 666322, 0, 0, 245884, 61769044, 1422460, 0, 0, 8410140, 0, 846980, 5159293, 32117770, 0, 9500290, 1205000, 0, 846980, 1477180, 782951, 0, 0, 5817522, 0, 5817522, 1, 9894154, 0, 9894154, 'A10023132'),
('7', '서울특별시', '강남구', '', '자곡동', '디아크리온강남 아파트', 202407, 64280229, 28530924, 1282464, 215870, 0, 0, 0, 754576, 14161230, 14037310, 410000, 1760000, 0, 1509120, 1230498, 0, 0, 388237, 75794120, 1422460, 0, 0, 8192340, 0, 883070, 5220842, 45033580, 0, 10517540, 1205000, 0, 883070, 1409790, 1026430, 0, 0, 5817522, 0, 11635044, 0, 3731069, 0, 3731069, 'A10023132'),
('8', '서울특별시', '강남구', '', '자곡동', '디아크리온강남 아파트', 202408, 61720235, 26017050, 770404, 255080, 0, 0, 0, 804456, 12796670, 15475500, 410000, 1760000, 0, 1347220, 1695618, 0, 0, 388237, 84434392, 1422460, 0, 0, 6250420, 0, 790970, 2511339, 58504112, 0, 10184050, 1205000, 0, 790970, 1086790, 1026420, 661860, 0, 5817522, 0, 17452566, 1, 3509941, 0, 3509941, 'A10023132'),
('9', '서울특별시', '강남구', '', '자곡동', '디아크리온강남 아파트', 202409, 64038585, 28280390, 904144, 200080, 0, 0, 0, 1033896, 12772800, 15071810, 410000, 1760000, 0, 1746010, 1471218, 0, 0, 388237, 71476840, 1422460, 0, 0, 6523580, 0, 719590, 6480350, 42393888, 0, 9506790, 1200000, 0, 719590, 1507200, 1003390, 0, 0, 5817522, 0, 23270088, 1, 3568833, 0, 3568833, 'A10023132'),
('10', '서울특별시', '강남구', '', '자곡동', '디아크리온강남 아파트', 202410, 61475485, 26122080, 916924, 213390, 105600, 113000, 0, 734496, 14017960, 14037310, 410000, 1760000, 0, 1006710, 1649778, 0, 0, 388237, 59922496, 1339520, 684760, 0, 11485140, 0, 737070, 6571825, 24790790, 0, 10040030, 1202500, 0, 737070, 1080400, 1003390, 250000, 0, 5817522, 0, 29087610, 1, 3675158, 0, 3675158, 'A10023132'),
('11', '서울특별시', '강남구', '', '자곡동', '디아크리온강남 아파트', 202411, 67028245, 26294280, 1125154, 205540, 143000, 0, 0, 5639056, 14108180, 14037310, 410000, 1760000, 0, 1267710, 1649778, 0, 0, 388237, 70191968, 1244290, 7900180, 0, 16353110, 0, 720080, 5408916, 24214520, 0, 9631690, 1185000, 0, 720080, 1532500, 1003390, 278210, 0, 5817522, 0, 30796132, 1, 5293613, 0, 5293613, 'A10023132');

INSERT INTO mng_cost (mngcost_id, province, city_county_district, town_village, street, complex_name, occurrence_year_month, individual_usage_sum, heating_cost_common, heating_cost_individual, hot_water_cost_common, hot_water_cost_individual, gas_usage_cost_common, gas_usage_cost_individual, electricity_cost_common, electricity_cost_individual, water_cost_common, water_cost_individual, tvfee, sewage_fee, waste_fee, association_cost, building_insurance_fee, election_cost, etc, reserve_fund_monthly_charge, reserve_fund_monthly_expenditure, reserve_fund_total_accumulated, reserve_fund_accumulation_rate, miscellaneous_income_monthly_amount, resident_contribution_revenue, common_contribution_revenue, total_common_management_fee_sum, labor_cost, office_expenses, taxes_and_dues, clothing_cost, training_cost, vehicle_maintenance_cost, other_incidental_expenses, cleaning_cost, security_cost, disinfection_cost, elevator_maintenance_cost, intelligent_network_maintenance, repair_cost, facility_maintenance_cost, safety_inspection_cost, disaster_prevention_cost, management_commission_fee, detail_apts_id) VALUES
('12', '경기도', '파주시', '', '금촌동', '금촌후곡마을4단지', 202401, 112780630, 36182060, 1179330, 132740, 112200, 22000, 0, 461950, 21472140, 35572392, 1042000, 3946800, 0, 10712010, 1000000, 0, 0, 945010, 163713296, 0, 0, 0, 0, 921210, 0, 3714530, 98298288, 0, 53546700, 0, 0, 2420660, 2203100, 2146800, 462000, 0, 67003740, 6160000, 1697234304, 0, 42968096, 5879615, 37088480, 'A41370508'),
('13', '경기도', '파주시', '', '금촌동', '금촌후곡마을4단지', 202402, 111539090, 35541888, 1848680, 132050, 0, 48000, 0, 338590, 21506340, 36835680, 1042000, 3946800, 0, 8354050, 1000000, 0, 0, 945010, 156662352, 0, 0, 0, 0, 1256100, 0, 11863240, 85750704, 0, 51558440, 0, 0, 2339540, 1428530, 2146800, 319000, 0, 67003740, 1282600, 1776967424, 0, 22291852, 6018992, 16272861, 'A41370508'),
('14', '경기도', '파주시', '', '금촌동', '금촌후곡마을4단지', 202403, 110584000, 35389840, 1310040, 132890, 0, 30000, 0, 342150, 21506040, 37066832, 1042000, 3946800, 0, 7872400, 1000000, 0, 0, 945010, 142717472, 0, 0, 0, 0, 897940, 0, 6750570, 78173768, 0, 50357072, 0, 0, 2268800, 1722530, 2146800, 400000, 0, 67003740, 0, 1863529728, 0, 35180564, 23562248, 11618316, 'A41370508'),
('15', '경기도', '파주시', '', '금촌동', '금촌후곡마을4단지', 202404, 115760670, 39719220, 1472850, 124060, 0, 35000, 0, 440010, 21545190, 36969860, 1042000, 3946800, 0, 8520670, 1000000, 0, 0, 945010, 147280816, 0, 0, 0, 0, 761460, 0, 3716010, 84284608, 0, 52425272, 0, 0, 2249570, 1374100, 2146800, 323000, 0, 67003740, 8399600, 1922167936, 0, 12726778, 483613, 12243165, 'A41370508'),
('16', '경기도', '파주시', '', '금촌동', '금촌후곡마을4단지', 202405, 114664190, 38902040, 1508890, 135020, 0, 0, 0, 392690, 21564840, 36969860, 1042000, 3946800, 0, 8257040, 1000000, 0, 0, 945010, 135606512, 0, 0, 0, 0, 131780, 0, 5138190, 73200680, 0, 51021712, 0, 0, 2434670, 1532680, 2146800, 0, 0, 67003740, 65560000, 1929414272, 0, 18976464, 7307418, 11669047, 'A41370508'),
('17', '경기도', '파주시', '', '금촌동', '금촌후곡마을4단지', 202406, 114232750, 38499740, 1828050, 131520, 183000, 0, 0, 412900, 21569040, 36969860, 1042000, 3946800, 0, 7704830, 1000000, 0, 0, 945010, 147767264, 0, 0, 0, 0, 0, 0, 6421800, 74548000, 0, 56714980, 3722500, 0, 2759180, 1224100, 2146800, 229900, 0, 67003740, 1584000, 1995140224, 0, 30258084, 837633, 29420452, 'A41370508'),
('18', '경기도', '파주시', '', '금촌동', '금촌후곡마을4단지', 202407, 114904180, 40124580, 1570010, 141190, 183000, 105000, 0, 389650, 21569040, 36969860, 1042000, 3946800, 0, 6918040, 1000000, 0, 0, 945010, 163393936, 0, 0, 0, 0, 142420, 0, 3637880, 94199240, 0, 55403872, 3737500, 0, 2803130, 1323100, 2146800, 0, 0, 67003740, 134548704, 1927854848, 0, 14153166, 756756, 13396410, 'A41370508'),
('19', '경기도', '파주시', '', '금촌동', '금촌후곡마을4단지', 202408, 115507780, 40581340, 1429900, 198250, 183000, 78000, 0, 319610, 21804240, 37105860, 1042000, 3946800, 0, 6873770, 1000000, 0, 0, 945010, 229995872, 0, 0, 0, 0, 0, 0, 3610660, 161351056, 0, 55103928, 3740000, 0, 2611920, 1431520, 2146800, 0, 0, 67003740, 141521600, 1853486976, 0, 15938003, 625670, 15312333, 'A41370508'),
('20', '경기도', '파주시', '', '금촌동', '금촌후곡마을4단지', 202409, 116161040, 40581340, 1329900, 152740, 183000, 30000, 0, 1683780, 21569040, 36969860, 1042000, 3946800, 0, 6727570, 1000000, 0, 0, 945010, 216644672, 0, 0, 0, 0, 0, 0, 4059730, 142937152, 0, 59250312, 3725000, 0, 2535250, 1500520, 2636700, 0, 0, 67003740, 6600000, 1913898624, 0, 11918205, 454909, 11463296, 'A41370508'),
('21', '경기도', '파주시', '', '금촌동', '금촌후곡마을4단지', 202410, 116281710, 40658840, 1542990, 149030, 183000, 35000, 0, 207980, 20886900, 36755728, 1042000, 3946800, 0, 8928430, 1000000, 0, 0, 945010, 158376768, 0, 0, 0, 0, 0, 0, 3729160, 92170720, 0, 52911128, 3715000, 0, 2345750, 1411100, 2093900, 0, 0, 67003740, 2607000, 1978301952, 0, 25229766, 450123, 24779644, 'A41370508'),
('22', '경기도', '파주시', '', '금촌동', '금촌후곡마을4단지', 202411, 115301950, 38489140, 2540860, 159790, 1301500, 0, 0, 237180, 21538440, 36967340, 1042000, 3946800, 0, 7133890, 1000000, 0, 0, 945010, 145144816, 0, 0, 0, 0, 171360, 0, 5235840, 75026944, 0, 54844100, 3730000, 0, 2674480, 1367900, 2094200, 0, 0, 67003740, 5529700, 2039780352, 0, 16419955, 447746, 15972209, 'A41370508');


-- 공지사항 더미 데이터
INSERT INTO my_apt.notices (created_at, notices_id, updated_at, content, title) VALUES ('2025-02-08 11:05:59.000000', 1, '2025-02-08 11:06:01.000000', '아파트 정보 조회 서비스가 런칭되었습니다.\\n많은 이용 부탁드립니다. 감사합니다.', '아파트 정보 조회 서비스 첫 서비스 시작..!(v1.0.0)');
INSERT INTO my_apt.notices (created_at, notices_id, updated_at, content, title) VALUES ('2025-02-10 11:07:36.000000', 2, '2025-02-10 11:07:38.000000', '뉴스 데이터 및 아파트 상세 정보 부분 버그가 수정되었습니다.', '서비스 업데이트(v1.0.1)');
INSERT INTO my_apt.notices (created_at, notices_id, updated_at, content, title) VALUES ('2025-02-12 11:08:43.000000', 3, '2025-02-12 11:08:44.000000', '현재 뉴스 데이터가 일반 아파트 정보와 부실 아파트 정보가 구분되지 않는 버그가 발생하였습니다. 조속히 해결 하도록 하겠습니다.\\n 감사합니다.', '버그 안내');
INSERT INTO my_apt.notices (created_at, notices_id, updated_at, content, title) VALUES ('2025-02-12 11:09:22.000000', 4, '2025-02-12 11:09:23.000000', '현재 사이트에 트래픽이 많이 발생함으로 불편을 야기할 수도 있습니다.', '트래픽 안내');
