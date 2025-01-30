package com.myapt.domain.apt.service;

import com.myapt.domain.apt.dto.AptInfoDetail;
import com.myapt.domain.apt.dto.AptInfo;
import com.myapt.domain.apt.entity.Apts;
import com.myapt.domain.apt.entity.DetailApts;
import com.myapt.domain.apt.entity.MngCost;
import com.myapt.domain.apt.repository.AptRepository;
import com.myapt.domain.apt.repository.DetailAptsRepository;
import com.myapt.domain.apt.repository.MngCostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AptServiceImpl implements AptService{
    private final AptRepository aptRepository;
    private final DetailAptsRepository detailAptsRepository;
    private final MngCostRepository mngCostRepository;

    @Override
    public AptInfo getApartmentInfo(String aptsId) {
        // #1. apts_id 를 사용해서, Apts 와 DetailApts의 리포지토리들로 부터 아파트 기본정보들을 받아온다.
        Apts apts = aptRepository.findById(aptsId).orElseThrow(() -> new RuntimeException("Apts not found"));
        DetailApts detailApts = detailAptsRepository.findByApts_Id(aptsId).orElseThrow(() -> new RuntimeException("DetailApts not found"));

        // #2. detail_apts_id(아파트 관리비코드)에 해당하는 월별 관리비를 계산 및 나머지 요소들과 함께 아파트기본정보를 반환하는 코드
        List<MngCost> mngCosts = mngCostRepository.findByDetailAptsId(detailApts.getId());

        // #3. 세대수 가져오기
        long numberOfUnits = apts.getNmhsh();

        // #4. 월별 관리비 계산 (세대수 이용)
        Map<String, Long> monthlyMaintenanceFees = mngCosts.stream()
                .collect(Collectors.toMap(
                        mngCost -> String.valueOf(mngCost.getOccurrenceYearMonth()),
                        mngCost -> {
                            long individualUsageSumPerUnit = mngCost.getIndividualUsageSum() / numberOfUnits;
                            long reserveFundMonthlyChargePerUnit = mngCost.getReserveFundMonthlyCharge() / numberOfUnits;
                            long totalCommonManagementFeeSumPerUnit = mngCost.getTotalCommonManagementFeeSum() / numberOfUnits;

                            return individualUsageSumPerUnit + reserveFundMonthlyChargePerUnit + totalCommonManagementFeeSumPerUnit;
                        }
                ));

        // #5. amenities 문자열을 List<String>으로 변환
        List<String> amenitiesList = Arrays.stream(detailApts.getAmenities().split(","))
                .map(String::trim) // 각 요소의 앞뒤 공백 제거
                .collect(Collectors.toList());

        // #6. AptInfo DTO 객체를 생성해서 모든 정보를 담는다.
        return AptInfo.of(
                apts.getAptNm(),
                detailApts.getComplexType(),
                apts.getRdnmadr(),
                detailApts.getPostalCode(),
                String.valueOf(apts.getUseAprvYear()),
                detailApts.getDeveloper(),
                detailApts.getConstructor(),
                numberOfUnits,
                monthlyMaintenanceFees,
                amenitiesList, // List<String>으로 전달
                apts.getBuldStru(),
                detailApts.getManagementType(),
                detailApts.getHeatingType(),
                detailApts.getCctvCount(),
                detailApts.getTotalParkingSpaces(),
                detailApts.getManagementOfficeAddress(),
                detailApts.getManagementOfficeContact(),
                detailApts.getManagementOfficeFax(),
                detailApts.getHousingManager()
        );
    }
    @Override
    public AptInfoDetail getApartmentInfoDetail(String detailAptsId) {
        // #1. apts_id 를 사용해서 DetailApts의 리포지토리로부터 아파트 상세정보들을 받아온다.
        DetailApts detailApts = detailAptsRepository.findById(detailAptsId).orElseThrow(() -> new RuntimeException("DetailApts not found"));

        // #2. AptInfoDetail DTO 객체를 생성해서 모든 정보를 담는다.
        return AptInfoDetail.of(
                detailApts.getMaxFloorCount(),
                detailApts.getBasementFloorCount(),
                detailApts.getPassengerCargoElevatorCount(), // 승용&화물 엘레베이터 수
                detailApts.getBuildingStructure(), // 건물구조
                detailApts.getApprovalDate(), // 사용승인일 (준공일)
                detailApts.getDeveloper(), // 시행사
                detailApts.getConstructor(), // 시공사
                detailApts.getNumberOfUnits(), // 세대수
                detailApts.getGroundParkingSpaces(), // 지상 주차 공간 수
                detailApts.getUndergroundParkingSpaces(), // 지하 주차 공간 수
                detailApts.getGroundEvChargerCount(), // 지상 전기차 충전기 수
                detailApts.getUndergroundEvChargerCount(), // 지하 전기차 충전기 수
                detailApts.getGroundEvParkingSpaces(), // 지상 전기차 주차 공간 수
                detailApts.getUndergroundEvParkingSpaces(), // 지하 전기차 주차 공간 수
                detailApts.getEvChargingFacilitiesDetails(), // 전기차 충전 시설 상세
                detailApts.getDisinfectionManagementType(), // 소독 관리 형태
                detailApts.getDisinfectionManagementContractor(), // 소독 관리 용역
                detailApts.getAnnualDisinfectionFrequency(), // 연간 소독 횟수
                detailApts.getSecurityManagementType(), // 경비 관리 형태
                detailApts.getSecurityManagementContractor(), // 경비 관리 용역
                detailApts.getSecurityManagementStaff(), // 경비 관리 인원
                detailApts.getCleaningManagementType(), // 청소 관리 형태
                detailApts.getCleaningManagementContractor(), // 청소 관리 용역
                detailApts.getFoodWasteDisposalMethod(), // 음식물 쓰레기 처리 방법
                detailApts.getGeneralManagementStaff() // 일반 관리 인원
        );
    }
}
