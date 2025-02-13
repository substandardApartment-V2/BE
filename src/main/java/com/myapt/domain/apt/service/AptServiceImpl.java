package com.myapt.domain.apt.service;

import com.myapt.domain.apt.dto.AptInfo;
import com.myapt.domain.apt.dto.AptInfoDetail;
import com.myapt.domain.apt.dto.LowestMgmtFeeAptInfo;
import com.myapt.domain.apt.dto.MainResponse;
import com.myapt.domain.apt.dto.MngCostInfo;
import com.myapt.domain.apt.dto.NoticeInfo;
import com.myapt.domain.apt.dto.NoticeRequest;
import com.myapt.domain.apt.dto.NoticeResponse;
import com.myapt.domain.apt.entity.Apts;
import com.myapt.domain.apt.entity.DetailApts;
import com.myapt.domain.apt.entity.MngCost;
import com.myapt.domain.apt.entity.Notices;
import com.myapt.domain.apt.exception.NoticeNotFoundException;
import com.myapt.domain.apt.repository.AptRepository;
import com.myapt.domain.apt.repository.DetailAptsRepository;
import com.myapt.domain.apt.repository.MngCostRepository;
import com.myapt.domain.apt.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AptServiceImpl implements AptService{
    private final AptRepository aptRepository;
    private final DetailAptsRepository detailAptsRepository;
    private final MngCostRepository mngCostRepository;
    private final NoticeRepository noticeRepository;

    @Override
    public MainResponse getMainInfo() {
        List<Apts> aptsList = aptRepository.findAll();

        Long aptAvgPrice = 685000000L; // 향후 DB에서 받아오도록 수정
        Long plannedAptCount = 87L;
        String lowestAptAddress = "울산시 북구 화봉동"; // 향후 DB에서 받아오도록 수정
        String lowestAptName = "행남아파트"; // 향후 DB에서 받아오도록 수정

       return MainResponse.of(
           aptAvgPrice,
		   (long)aptsList.size(), // 아파트 개수
           plannedAptCount,
           LowestMgmtFeeAptInfo.of(
               lowestAptAddress,
               lowestAptName
           )
       );
    }
    @Override
    public NoticeInfo getNotice(Long id) {
        if (id == null) { throw new IllegalArgumentException("id 항목이 누락되었습니다."); }
        Notices notice = noticeRepository.findById(id).orElseThrow(() -> new NoticeNotFoundException());
        return NoticeInfo.of(
            notice.getId(),
            notice.getTitle(),
            notice.getContent(),
            notice.getCreateAt()
        );
    }

    @Override
    public NoticeResponse getNotices(NoticeRequest noticeRequest) {
        Integer pages = noticeRequest.pages();
        Integer num = noticeRequest.num();

        if (num == null) { throw new IllegalArgumentException("num 항목이 누락되었습니다."); }
        if (pages == null) { throw new IllegalArgumentException("pages 항목이 누락되었습니다."); }
        List<Notices> notices = noticeRepository.findAll(
            PageRequest.of(pages, num, Sort.by(Sort.Direction.DESC, "createdAt"))).getContent();

        // 공지 사항 없을 경우 404
        if (notices.isEmpty()) {
            throw new NoticeNotFoundException();
        }

        List<NoticeInfo> noticeInfoList = notices.stream()
            .map(notice -> NoticeInfo.of(
                notice.getId(),
                notice.getTitle(),
                notice.getContent().length() > 100 ? notice.getContent().substring(0, 100) : notice.getContent(), // 100자 제한
                notice.getCreateAt()
            ))
            .collect(Collectors.toList());

        return NoticeResponse.of(noticeInfoList, noticeRepository.count());
    }

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
                detailApts.getId(),
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

        // 전기차 충전 시설 상세 정보를 문자열로 받아옵니다. (예: "◆1◆|지하|스탠드형충전기|AC단상 5핀|완속|2|kepco|,◆2◆|지하|스탠드형충전기|AC3상 7핀|급속|1|kepco|")
        String evChargingDetailsString = detailApts.getEvChargingFacilitiesDetails();
        // 문자열을 파싱하여 리스트를 채웁니다.
        List<AptInfoDetail.EvChargingFacilityDetail> evChargingFacilitiesDetails = parseEvChargingDetails(evChargingDetailsString);

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
                evChargingFacilitiesDetails, // 전기차 충전 시설 상세
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

    @Override
    public MngCostInfo getMngCostInfoDetail(String detailAptsId) {
        // #1. detailAptsId를 사용하여 MngCost 리포지토리로부터 해당 아파트 관리비 상세정보들을 받아온다.
        List<MngCost> mngCosts = mngCostRepository.findByDetailAptsId(detailAptsId);

        // #2. MngCostInfo DTO 객체를 생성하여 모든 정보를 담는다.

        // #2-2 월별 공용관리비 상세 내역을 리스트로 변환
        List<MngCostInfo.MonthlyCommonManagementFee> monthlyCommonManagementFeeList = mngCosts.stream()
                .map(mngCost -> new MngCostInfo.MonthlyCommonManagementFee(
                        mngCost.getOccurrenceYearMonth(), // 발생 년월로 수정
                        mngCost.getLaborCost(),
                        mngCost.getOfficeExpenses(),
                        mngCost.getTaxesAndDues(),
                        mngCost.getClothingCost(),
                        mngCost.getTrainingCost(),
                        mngCost.getVehicleMaintenanceCost(),
                        mngCost.getOtherIncidentalExpenses(),
                        mngCost.getCleaningCost(),
                        mngCost.getSecurityCost(),
                        mngCost.getDisinfectionCost(),
                        mngCost.getElevatorMaintenanceCost(),
                        mngCost.getIntelligentNetworkMaintenance(),
                        mngCost.getRepairCost(),
                        mngCost.getFacilityMaintenanceCost(),
                        mngCost.getSafetyInspectionCost(),
                        mngCost.getDisasterPreventionCost(),
                        mngCost.getManagementCommissionFee()
                ))
                .sorted(Comparator.comparing(MngCostInfo.MonthlyCommonManagementFee::occurrenceYearMonth)) // 오름차순 정렬
                .collect(Collectors.toList());

        // #2-3 월별 개별관리비 상세 내역을 리스트로 변환
        List<MngCostInfo.MonthlyIndividualManagementFee> monthlyIndividualManagementFeeList = mngCosts.stream()
                .map(mngCost -> new MngCostInfo.MonthlyIndividualManagementFee(
                        mngCost.getOccurrenceYearMonth(), // 발생 년월로 수정
                        mngCost.getHeatingCostCommon(),
                        mngCost.getHeatingCostIndividual(),
                        mngCost.getHotWaterCostCommon(),
                        mngCost.getHotWaterCostIndividual(),
                        mngCost.getGasUsageCostCommon(),
                        mngCost.getGasUsageCostIndividual(),
                        mngCost.getElectricityCostCommon(),
                        mngCost.getElectricityCostIndividual(),
                        mngCost.getWaterCostCommon(),
                        mngCost.getWaterCostIndividual(),
                        mngCost.getTvFee(),
                        mngCost.getSewageFee(),
                        mngCost.getWasteFee(),
                        mngCost.getAssociationCost(),
                        mngCost.getBuildingInsuranceFee(),
                        mngCost.getElectionCost(),
                        mngCost.getEtc()
                ))
                .sorted(Comparator.comparing(MngCostInfo.MonthlyIndividualManagementFee::occurrenceYearMonth)) // 오름차순 정렬
                .collect(Collectors.toList());

        // #2-4 월별 잡수입 상세 내역을 리스트로 변환
        List<MngCostInfo.MiscellaneousIncomeMonthlyAmount> miscellaneousIncomeMonthlyAmountList = mngCosts.stream()
                .map(mngCost -> new MngCostInfo.MiscellaneousIncomeMonthlyAmount(
                        mngCost.getOccurrenceYearMonth(), // 발생 년월로 수정
                        mngCost.getMiscellaneousIncomeMonthlyAmount(),
                        mngCost.getResidentContributionRevenue(),
                        mngCost.getCommonContributionRevenue()
                ))
                .sorted(Comparator.comparing(MngCostInfo.MiscellaneousIncomeMonthlyAmount::occurrenceYearMonth)) // 오름차순 정렬
                .collect(Collectors.toList());

        // #2-5 장충금 월부과액 상세 내역을 Map으로 변환 (월별 부과액)
        Map<Long, Long> reserveFundMonthlyCharge = mngCosts.stream()
                .sorted(Comparator.comparing(MngCost::getOccurrenceYearMonth)) // 오름차순 정렬
                .collect(Collectors.toMap(
                        MngCost::getOccurrenceYearMonth,
                        MngCost::getReserveFundMonthlyCharge,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        // #2-6 장충금 월사용액 상세 내역을 Map으로 변환 (월별 사용액)
        Map<Long, Long> reserveFundMonthlyExpenditure = mngCosts.stream()
                .sorted(Comparator.comparing(MngCost::getOccurrenceYearMonth)) // 오름차순 정렬
                .collect(Collectors.toMap(
                        MngCost::getOccurrenceYearMonth,
                        MngCost::getReserveFundMonthlyExpenditure,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        // #2-7 장충금 총적립금액 상세 내역을 Map으로 변환 (월별 총적립액)
        Map<Long, Long> reserveFundTotalAccumulated = mngCosts.stream()
                .sorted(Comparator.comparing(MngCost::getOccurrenceYearMonth)) // 오름차순 정렬
                .collect(Collectors.toMap(
                        MngCost::getOccurrenceYearMonth,
                        MngCost::getReserveFundTotalAccumulated,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        // #2-8 장충금 적립율 상세 내역을 Map으로 변환 (월별 적립률)
        Map<Long, Long> reserveFundAccumulationRate = mngCosts.stream()
                .sorted(Comparator.comparing(MngCost::getOccurrenceYearMonth)) // 오름차순 정렬
                .collect(Collectors.toMap(
                        MngCost::getOccurrenceYearMonth,
                        MngCost::getReserveFundAccumulationRate,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        // #3. MngCostInfo DTO를 빌드하여 모든 정보를 담아 반환
        return MngCostInfo.builder()
                .monthlyTotalCommonManagementFeeSum(monthlyCommonManagementFeeList) // 공용관리비 상세
                .monthlyTotalIndividualManagementFeeSum(monthlyIndividualManagementFeeList) // 개별관리비 상세
                .reserveFundMonthlyCharge(reserveFundMonthlyCharge) // 장충금 월부과액
                .reserveFundMonthlyExpenditure(reserveFundMonthlyExpenditure) // 장충금 월사용액
                .reserveFundTotalAccumulated(reserveFundTotalAccumulated) // 장충금 총적립금액
                .reserveFundAccumulationRate(reserveFundAccumulationRate) // 장충금 적립율
                .miscellaneousIncomeMonthlyAmount(miscellaneousIncomeMonthlyAmountList) // 잡수입 월수입금액
                .build();
    }

    // evChargingFacilitiesDetails 의 문자열을 파싱하는 함수
    public static List<AptInfoDetail.EvChargingFacilityDetail> parseEvChargingDetails(String data) {
        List<AptInfoDetail.EvChargingFacilityDetail> details = new ArrayList<>();
        // 데이터 항목을 구분자로 분리
        String[] items = data.split(",(?=◆)");
        for (String item : items) {
            // 앞의 ◆1◆ 같은 부분을 제거하고 남은 부분을 |로 분리
            String[] parts = item.replaceAll("^◆\\d+◆\\|", "").split("\\|");
            // 각 속성을 추출하여 객체로 만듭니다.
            if (parts.length >= 6) {  // 최소 6개의 요소가 있어야 함
                try {
                    String location = parts[0]; // 위치
                    String type = parts[1]; // 충전기 타입
                    String connector = parts[2]; // 커넥터 타입
                    String chargingSpeed = parts[3]; // 충전 속도
                    int count = Integer.parseInt(parts[4]); // 충전기 대수
                    String provider = parts[5]; // 공급자
                    // 객체 생성
                    AptInfoDetail.EvChargingFacilityDetail detail = new AptInfoDetail.EvChargingFacilityDetail(
                        location, type, connector, chargingSpeed, count, provider);
                    // 리스트에 추가
                    details.add(detail);
                } catch (NumberFormatException e) {
                    // 파싱 오류에 대한 예외 처리
                    System.err.println("Error parsing count: " + parts[4]);
                }
            } else {
                // parts의 길이가 예상과 다를 경우에 대한 처리
                System.err.println("Unexpected data format: " + item);
            }
        }
        return details;
    }
}
