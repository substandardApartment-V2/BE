package com.myapt.domain.apt.service;

import com.myapt.domain.apt.dto.*;
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

import java.util.*;
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
        String sort = noticeRequest.sort();

        if (num == null) { throw new IllegalArgumentException("num 항목이 누락되었습니다."); }
        if (pages == null) { throw new IllegalArgumentException("pages 항목이 누락되었습니다."); }
        if (sort == null) { throw new IllegalArgumentException("sort 항목이 누락되었습니다."); }

        // 최신순 or 오랜된 순 (default, 잘못된 값일 경우 DESC)
        Sort.Direction direction = sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        List<Notices> notices = noticeRepository.findAll(
            PageRequest.of(pages, num, Sort.by(direction, "createdAt"))).getContent();

        // 공지 사항 없을 경우 404
        if (notices.isEmpty()) {
            throw new NoticeNotFoundException();
        }

        List<NoticeInfo> noticeInfoList = notices.stream()
            .map(notice -> NoticeInfo.of(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCreateAt()
            ))
            .collect(Collectors.toList());

        return NoticeResponse.of(noticeInfoList, noticeRepository.count());
    }

    @Override
    public AptInfo getApartmentInfo(String aptsId) {
        // #1. apts_id 를 사용해서, Apts 와 DetailApts의 리포지토리들로 부터 아파트 기본정보들을 받아온다.
        Apts apts = aptRepository.findById(aptsId).orElseThrow(() -> new RuntimeException("아파트 기본정보 데이터를 조회할 수 없습니다."));
        DetailApts detailApts = detailAptsRepository.findByApts_Id(aptsId).orElseThrow(() -> new RuntimeException("아파트 상세정보 데이터를 조회할 수 없습니다."));

        // #2. detail_apts_id(아파트 관리비코드)에 해당하는 월별 관리비를 계산 및 나머지 요소들과 함께 아파트기본정보를 반환하는 코드
        List<MngCost> mngCosts = defaultIfNull(mngCostRepository.findByDetailAptsId(detailApts.getId()), Collections.emptyList());

        // #3. 세대수 가져오기
        long numberOfUnits = defaultIfNull(apts.getNmhsh(), 0).longValue();

        // #4. 월별 관리비 계산 (세대수 이용)
        Map<String, Long> monthlyMaintenanceFees = mngCosts.stream()
                .collect(Collectors.toMap(
                        mngCost -> String.valueOf(defaultIfNull(mngCost.getOccurrenceYearMonth(), 0)),
                        mngCost -> {
                            long individualUsageSumPerUnit = defaultIfNull(mngCost.getIndividualUsageSum(), 0L) / numberOfUnits;
                            long reserveFundMonthlyChargePerUnit = defaultIfNull(mngCost.getReserveFundMonthlyCharge(), 0L) / numberOfUnits;
                            long totalCommonManagementFeeSumPerUnit = defaultIfNull(mngCost.getTotalCommonManagementFeeSum(), 0L) / numberOfUnits;

                            return individualUsageSumPerUnit + reserveFundMonthlyChargePerUnit + totalCommonManagementFeeSumPerUnit;
                        }
                ));

        // #5. amenities 문자열을 List<String>으로 변환
        List<String> amenitiesList = Arrays.stream(defaultIfNull(detailApts.getAmenities(), "").split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        // #6. 총 전기차 충전기 대수 계산
        long totalGroundEvChargerCount = defaultIfNull(detailApts.getGroundEvChargerCount(), 0).longValue() +
                defaultIfNull(detailApts.getUndergroundEvChargerCount(), 0).longValue();

        // #7. AptInfo DTO 객체를 생성해서 모든 정보를 담는다.
        return AptInfo.of(
                defaultIfNull(detailApts.getId(), ""),
                defaultIfNull(apts.getAptNm(), ""),
                defaultIfNull(detailApts.getComplexType(), ""),
                defaultIfNull(apts.getRdnmadr(), ""),
                defaultIfNull(detailApts.getPostalCode(), ""),
                defaultIfNull(String.valueOf(apts.getUseAprvYear()), "0"),
                defaultIfNull(detailApts.getDeveloper(), ""),
                defaultIfNull(detailApts.getConstructor(), ""),
                numberOfUnits,
                monthlyMaintenanceFees,
                amenitiesList, // List<String>으로 전달
                defaultIfNull(apts.getBuldStru(), ""),
                defaultIfNull(detailApts.getManagementType(), ""),
                defaultIfNull(detailApts.getHeatingType(), ""),
                defaultIfNull(detailApts.getCctvCount(), 0).longValue(),
                defaultIfNull(detailApts.getTotalParkingSpaces(), 0).longValue(),
                totalGroundEvChargerCount, // 총 전기차 충전기 대수 설정
                defaultIfNull(detailApts.getManagementOfficeAddress(), ""),
                defaultIfNull(detailApts.getManagementOfficeContact(), ""),
                defaultIfNull(detailApts.getManagementOfficeFax(), ""),
                defaultIfNull(detailApts.getHousingManager(), "")
        );
    }

    @Override
    public AptInfoDetail getApartmentInfoDetail(String detailAptsId) {
        // #1. apts_id 를 사용해서 DetailApts의 리포지토리로부터 아파트 상세정보들을 받아온다.
        DetailApts detailApts = detailAptsRepository.findById(detailAptsId)
                .orElseThrow(() -> new RuntimeException("해당 아파트 상세정보가 서버에 존재하지 않습니다."));

        // 전기차 충전 시설 상세 정보를 문자열로 받아옵니다.
        String evChargingDetailsString = detailApts.getEvChargingFacilitiesDetails();
        // 문자열을 파싱하여 리스트를 채웁니다.
        List<AptInfoDetail.EvChargingFacilityDetail> evChargingFacilitiesDetails = parseEvChargingDetails(
                evChargingDetailsString != null ? evChargingDetailsString : "");

        // #2. AptInfoDetail DTO 객체를 생성해서 모든 정보를 담는다.
        return AptInfoDetail.of(
                defaultIfNull(detailApts.getMaxFloorCount(), 0L),
                defaultIfNull(detailApts.getBasementFloorCount(), 0L),
                defaultIfNull(detailApts.getPassengerCargoElevatorCount(), 0L),
                defaultIfNull(detailApts.getBuildingStructure(), ""),
                defaultIfNull(detailApts.getApprovalDate(), ""),
                defaultIfNull(detailApts.getDeveloper(), ""),
                defaultIfNull(detailApts.getConstructor(), ""),
                defaultIfNull(detailApts.getNumberOfUnits(), 0L),
                defaultIfNull(detailApts.getGroundAccessibleToPublic(), false), // 외부인 개방 여부(지상) 추가
                defaultIfNull(detailApts.getUndergroundAccessibleToPublic(), false), // 외부인 개방 여부(지하) 추가
                defaultIfNull(detailApts.getGroundParkingSpaces(), 0L),
                defaultIfNull(detailApts.getUndergroundParkingSpaces(), 0L),
                defaultIfNull(detailApts.getGroundEvChargerCount(), 0L),
                defaultIfNull(detailApts.getUndergroundEvChargerCount(), 0L),
                defaultIfNull(detailApts.getGroundEvParkingSpaces(), 0L),
                defaultIfNull(detailApts.getUndergroundEvParkingSpaces(), 0L),
                evChargingFacilitiesDetails,
                defaultIfNull(detailApts.getDisinfectionManagementType(), ""),
                defaultIfNull(detailApts.getDisinfectionManagementContractor(), ""),
                defaultIfNull(detailApts.getAnnualDisinfectionFrequency(), 0L),
                defaultIfNull(detailApts.getSecurityManagementType(), ""),
                defaultIfNull(detailApts.getSecurityManagementContractor(), ""),
                defaultIfNull(detailApts.getSecurityManagementStaff(), 0L),
                defaultIfNull(detailApts.getCleaningManagementType(), ""),
                defaultIfNull(detailApts.getCleaningManagementContractor(), ""),
                defaultIfNull(detailApts.getFoodWasteDisposalMethod(), ""),
                defaultIfNull(detailApts.getGeneralManagementStaff(), 0L)
        );
    }
    // defaultIfNull 메소드를 사용하여 위에서 각 필드를 검사하고, null인 경우 기본 값을 반환하도록 했다.
    private <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    @Override
    public MngCostInfo getMngCostInfoDetail(String detailAptsId) {
        // #1. detailAptsId를 사용하여 MngCost 리포지토리로부터 해당 아파트 관리비 상세정보들을 받아온다.
        List<MngCost> mngCosts = mngCostRepository.findByDetailAptsId(detailAptsId);

        // #2-2 월별 공용관리비 상세 내역을 리스트로 변환
        List<MngCostInfo.MonthlyCommonManagementFee> monthlyCommonManagementFeeList = mngCosts.stream()
                .map(mngCost -> new MngCostInfo.MonthlyCommonManagementFee(
                        defaultIfNull(mngCost.getOccurrenceYearMonth(), 0L),
                        defaultIfNull(mngCost.getLaborCost(), 0L),
                        defaultIfNull(mngCost.getOfficeExpenses(), 0L),
                        defaultIfNull(mngCost.getTaxesAndDues(), 0L),
                        defaultIfNull(mngCost.getClothingCost(), 0L),
                        defaultIfNull(mngCost.getTrainingCost(), 0L),
                        defaultIfNull(mngCost.getVehicleMaintenanceCost(), 0L),
                        defaultIfNull(mngCost.getOtherIncidentalExpenses(), 0L),
                        defaultIfNull(mngCost.getCleaningCost(), 0L),
                        defaultIfNull(mngCost.getSecurityCost(), 0L),
                        defaultIfNull(mngCost.getDisinfectionCost(), 0L),
                        defaultIfNull(mngCost.getElevatorMaintenanceCost(), 0L),
                        defaultIfNull(mngCost.getIntelligentNetworkMaintenance(), 0L),
                        defaultIfNull(mngCost.getRepairCost(), 0L),
                        defaultIfNull(mngCost.getFacilityMaintenanceCost(), 0L),
                        defaultIfNull(mngCost.getSafetyInspectionCost(), 0L),
                        defaultIfNull(mngCost.getDisasterPreventionCost(), 0L),
                        defaultIfNull(mngCost.getManagementCommissionFee(), 0L)
                ))
                .sorted(Comparator.comparing(
                        MngCostInfo.MonthlyCommonManagementFee::occurrenceYearMonth,
                        Comparator.nullsLast(Long::compareTo))) // null을 마지막에 배치
                .collect(Collectors.toList());

        // #2-3 월별 개별관리비 상세 내역을 리스트로 변환
        List<MngCostInfo.MonthlyIndividualManagementFee> monthlyIndividualManagementFeeList = mngCosts.stream()
                .map(mngCost -> new MngCostInfo.MonthlyIndividualManagementFee(
                        defaultIfNull(mngCost.getOccurrenceYearMonth(), 0L),
                        defaultIfNull(mngCost.getHeatingCostCommon(), 0L),
                        defaultIfNull(mngCost.getHeatingCostIndividual(), 0L),
                        defaultIfNull(mngCost.getHotWaterCostCommon(), 0L),
                        defaultIfNull(mngCost.getHotWaterCostIndividual(), 0L),
                        defaultIfNull(mngCost.getGasUsageCostCommon(), 0L),
                        defaultIfNull(mngCost.getGasUsageCostIndividual(), 0L),
                        defaultIfNull(mngCost.getElectricityCostCommon(), 0L),
                        defaultIfNull(mngCost.getElectricityCostIndividual(), 0L),
                        defaultIfNull(mngCost.getWaterCostCommon(), 0L),
                        defaultIfNull(mngCost.getWaterCostIndividual(), 0L),
                        defaultIfNull(mngCost.getTvFee(), 0L),
                        defaultIfNull(mngCost.getSewageFee(), 0L),
                        defaultIfNull(mngCost.getWasteFee(), 0L),
                        defaultIfNull(mngCost.getAssociationCost(), 0L),
                        defaultIfNull(mngCost.getBuildingInsuranceFee(), 0L),
                        defaultIfNull(mngCost.getElectionCost(), 0L),
                        defaultIfNull(mngCost.getEtc(), 0L)
                ))
                .sorted(Comparator.comparing(
                        MngCostInfo.MonthlyIndividualManagementFee::occurrenceYearMonth,
                        Comparator.nullsLast(Long::compareTo))) // null을 마지막에 배치
                .collect(Collectors.toList());

        // #2-4 월별 잡수입 상세 내역을 리스트로 변환
        List<MngCostInfo.MiscellaneousIncomeMonthlyAmount> miscellaneousIncomeMonthlyAmountList = mngCosts.stream()
                .map(mngCost -> new MngCostInfo.MiscellaneousIncomeMonthlyAmount(
                        defaultIfNull(mngCost.getOccurrenceYearMonth(), 0L),
                        defaultIfNull(mngCost.getMiscellaneousIncomeMonthlyAmount(), 0L),
                        defaultIfNull(mngCost.getResidentContributionRevenue(), 0L),
                        defaultIfNull(mngCost.getCommonContributionRevenue(), 0L)
                ))
                .sorted(Comparator.comparing(
                        MngCostInfo.MiscellaneousIncomeMonthlyAmount::occurrenceYearMonth,
                        Comparator.nullsLast(Long::compareTo))) // null을 마지막에 배치
                .collect(Collectors.toList());

        // #2-5 장충금 월부과액 상세 내역을 Map으로 변환 (월별 부과액)
        Map<Long, Long> reserveFundMonthlyCharge = mngCosts.stream()
                .sorted(Comparator.comparing(
                        MngCost::getOccurrenceYearMonth,
                        Comparator.nullsLast(Long::compareTo))) // null을 마지막에 배치
                .collect(Collectors.toMap(
                        mngCost -> defaultIfNull(mngCost.getOccurrenceYearMonth(), 0L),
                        mngCost -> defaultIfNull(mngCost.getReserveFundMonthlyCharge(), 0L),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        // #2-6 장충금 월사용액 상세 내역을 Map으로 변환 (월별 사용액)
        Map<Long, Long> reserveFundMonthlyExpenditure = mngCosts.stream()
                .sorted(Comparator.comparing(
                        MngCost::getOccurrenceYearMonth,
                        Comparator.nullsLast(Long::compareTo))) // null을 마지막에 배치
                .collect(Collectors.toMap(
                        mngCost -> defaultIfNull(mngCost.getOccurrenceYearMonth(), 0L),
                        mngCost -> defaultIfNull(mngCost.getReserveFundMonthlyExpenditure(), 0L),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        // #2-7 장충금 총적립금액 상세 내역을 Map으로 변환 (월별 총적립액)
        Map<Long, Long> reserveFundTotalAccumulated = mngCosts.stream()
                .sorted(Comparator.comparing(
                        MngCost::getOccurrenceYearMonth,
                        Comparator.nullsLast(Long::compareTo))) // null을 마지막에 배치
                .collect(Collectors.toMap(
                        mngCost -> defaultIfNull(mngCost.getOccurrenceYearMonth(), 0L),
                        mngCost -> defaultIfNull(mngCost.getReserveFundTotalAccumulated(), 0L),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        // #2-8 장충금 적립율 상세 내역을 Map으로 변환 (월별 적립률)
        Map<Long, Long> reserveFundAccumulationRate = mngCosts.stream()
                .sorted(Comparator.comparing(
                        MngCost::getOccurrenceYearMonth,
                        Comparator.nullsLast(Long::compareTo))) // null을 마지막에 배치
                .collect(Collectors.toMap(
                        mngCost -> defaultIfNull(mngCost.getOccurrenceYearMonth(), 0L),
                        mngCost -> defaultIfNull(mngCost.getReserveFundAccumulationRate(), 0L),
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
                    long count = Integer.parseInt(parts[4]); // 충전기 대수
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
