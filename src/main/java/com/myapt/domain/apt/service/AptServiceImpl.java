package com.myapt.domain.apt.service;

import com.myapt.domain.apt.dto.*;
import com.myapt.domain.apt.entity.*;
import com.myapt.domain.apt.exception.MainInvalidException;
import com.myapt.domain.apt.exception.MainNotFoundException;
import com.myapt.domain.apt.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AptServiceImpl implements AptService{
    private static final Logger log = LoggerFactory.getLogger(AptServiceImpl.class);
    private final AptRepository aptRepository;
    private final DetailAptsRepository detailAptsRepository;
    private final MngCostRepository mngCostRepository;
    private final NoticeRepository noticeRepository;
    private final AvgPricesRepository avgPricesRepository;
    private final PlannedAptsRepository plannedAptsRepository;

    // 메인화면 정보 조회
    @Override
    public MainResponse getMainInfo() {
        // 함수가 호출될 때의 현재 연도와 월을 가져옴
        Long currentYear = (long) LocalDate.now().getYear();
        Long currentMonth = (long) LocalDate.now().getMonthValue();

        // 1. DB에서 가장 최근 연도와 월에 해당하는 AvgPrices 엔티티를 조회하고 값 설정
        Optional<AvgPrices> latestAvgPriceOpt = avgPricesRepository.findTopByOrderByYearDescMonthDesc();
        Long aptAvgPrice = latestAvgPriceOpt.map(AvgPrices::getAvgPrice).orElse(null); // 평균 가격
        Long aptAvgPriceMonth = latestAvgPriceOpt.map(AvgPrices::getMonth).orElse(null); // 가장 최근 월
        aptAvgPrice = (aptAvgPrice == null || aptAvgPrice == 0L) ? null : aptAvgPrice;

        // 2. 전국 아파트 수 - JpaRepository의 count() 메서드로 Apts 엔티티 총 개수 조회
        Long aptCount = aptRepository.count();
        aptCount = (aptCount == null || aptCount == 0L) ? null : aptCount;

        // 3. 현재 연도와 월에 해당하는 PlannedApts 엔티티를 조회하고 값 설정
        Long plannedAptCount = plannedAptsRepository.findByYearAndMonth(currentYear, currentMonth)
                .map(PlannedApts::getCount)
                .orElse(null);
        plannedAptCount = (plannedAptCount == null || plannedAptCount == 0L) ? null : plannedAptCount;

        // MainResponse 객체를 생성하여 반환
        return MainResponse.of(
                aptAvgPrice,
                aptAvgPriceMonth, // 가장 최근 월로 변경
                aptCount,
                plannedAptCount,
                currentYear,
                currentMonth
        );
    }

    // 공지사항 조회 API
    @Override
    public NoticeInfo getNotice(Long id) {
        Notices notice = noticeRepository.findById(id).orElseThrow(MainNotFoundException::noticeNotFound);
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
        String sortType = noticeRequest.sort();

        // 최신순 or 오랜된 순 (default, 잘못된 값일 경우 DESC)
        Sort.Direction direction;
        if(sortType.equalsIgnoreCase("ASC")){
            direction = Sort.Direction.ASC;
        } else if(sortType.equalsIgnoreCase("DESC")){
            direction = Sort.Direction.DESC;
        } else {
            throw MainInvalidException.sortTypeInvalid();
        }

        List<Notices> notices;
        try{
            notices = noticeRepository.findAll(
                    PageRequest.of(pages, num, Sort.by(direction, "createdAt"))).getContent();
        } catch (Exception e){
            log.error("공지사항 조회 오류 : {}", e.getMessage());
            throw MainNotFoundException.noticeFetchFailed();
        }

        if (notices.isEmpty()) {
            // 공지 사항 없을 경우
            return NoticeResponse.of(null, 0L);
        }
        else {
            // 공지 사항 있는 경우
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
    }

    // 아파트 기본 정보 조회
    @Override
    public AptInfo getApartmentInfo(String aptsId) {
        // #1. apts_id를 사용해서, Apts와 DetailApts의 리포지토리들로부터 아파트 기본 정보를 받아온다.
        Apts apts = aptRepository.findById(aptsId).orElseThrow(() -> new RuntimeException("아파트 기본정보 데이터를 조회할 수 없습니다."));
        Optional<DetailApts> optionalDetailApts = detailAptsRepository.findByApts_Id(aptsId);
        DetailApts detailApts = optionalDetailApts.orElse(new DetailApts()); // 기본값을 가지는 새로운 객체 생성

        // #2. detail_apts_id(아파트 관리비 코드)에 해당하는 월별 관리비를 계산 및 나머지 요소들과 함께 아파트 기본정보를 반환하는 코드
        List<MngCost> mngCosts = mngCostRepository.findByDetailAptsId(detailApts.getId());

        // #3. 세대수 가져오기 (0이면 null로 처리)
        Long numberOfUnits = (detailApts.getNumberOfUnits() == null || detailApts.getNumberOfUnits() == 0L)
                ? null : detailApts.getNumberOfUnits();

        // #4. 월별 관리비 계산 (세대수 이용)
        List<AptInfo.MonthlyMaintenanceData> monthlyMaintenanceData = (mngCosts == null || mngCosts.isEmpty())
                ? null
                : mngCosts.stream()
                .map(mngCost -> {
                    String occurrenceYearMonth = String.valueOf(mngCost.getOccurrenceYearMonth());
                    String month = occurrenceYearMonth.substring(4, 6);

                    Long fee = (numberOfUnits != null && numberOfUnits > 0)
                            ? (mngCost.getIndividualUsageSum() != null && mngCost.getIndividualUsageSum() > 0 ? mngCost.getIndividualUsageSum() / numberOfUnits : 0L) +
                            (mngCost.getReserveFundMonthlyCharge() != null && mngCost.getReserveFundMonthlyCharge() > 0 ? mngCost.getReserveFundMonthlyCharge() / numberOfUnits : 0L) +
                            (mngCost.getTotalCommonManagementFeeSum() != null && mngCost.getTotalCommonManagementFeeSum() > 0 ? mngCost.getTotalCommonManagementFeeSum() / numberOfUnits : 0L)
                            : null;

                    // 계산된 fee가 0이면 null로 처리
                    return new AptInfo.MonthlyMaintenanceData(month, fee == null || fee == 0L ? null : fee);
                })
                .sorted(Comparator.comparing(AptInfo.MonthlyMaintenanceData::month)) // 월별 정렬
                .collect(Collectors.toList());
        // 리스트가 비어있거나 모든 값이 null이면 null로 처리
        monthlyMaintenanceData = (monthlyMaintenanceData == null || monthlyMaintenanceData.stream().allMatch(d -> d.fee() == null)) ? null : monthlyMaintenanceData;

        // #5. 연도 정보 추출 (첫 번째 관리비 기록의 연도 사용)
        String year = (mngCosts == null || mngCosts.isEmpty())
                ? null
                : mngCosts.stream()
                .findFirst()
                .map(mngCost -> String.valueOf(mngCost.getOccurrenceYearMonth()).substring(0, 4))
                .filter(y -> !y.isEmpty()) // 빈 문자열 체크
                .orElse(null);

        // #6. amenities 문자열을 List<String>으로 변환
        List<String> amenitiesList = (detailApts.getAmenities() == null || detailApts.getAmenities().isEmpty() || detailApts.getAmenities().trim().isEmpty())
                ? null
                : Arrays.stream(detailApts.getAmenities().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        // #7. 총 전기차 충전기 대수 계산
        Long totalGroundEvChargerCount = (detailApts.getGroundEvChargerCount() == null && detailApts.getUndergroundEvChargerCount() == null)
                ? null
                : ((detailApts.getGroundEvChargerCount() != null && detailApts.getGroundEvChargerCount() > 0 ? detailApts.getGroundEvChargerCount() : 0L) +
                (detailApts.getUndergroundEvChargerCount() != null && detailApts.getUndergroundEvChargerCount() > 0 ? detailApts.getUndergroundEvChargerCount() : 0L));
        totalGroundEvChargerCount = (totalGroundEvChargerCount == null || totalGroundEvChargerCount == 0L) ? null : totalGroundEvChargerCount;

        // #8. completionDate 처리 (Integer 타입 가정)
        String completionDate = (apts.getUseAprvYear() == null || apts.getUseAprvYear() == 0)
                ? null : String.valueOf(apts.getUseAprvYear());

        // #9. AptInfo DTO 객체를 생성해서 모든 정보를 담는다.
        // null, 0, 빈 문자열 체크 추가
        String aptNm = (apts.getAptNm() == null || apts.getAptNm().trim().isEmpty()) ? null : apts.getAptNm();
        String rdnmadr = (apts.getRdnmadr() == null || apts.getRdnmadr().trim().isEmpty()) ? null : apts.getRdnmadr();
        String postalCode = (detailApts.getPostalCode() == null || detailApts.getPostalCode().trim().isEmpty()) ? null : detailApts.getPostalCode();
        String developer = (detailApts.getDeveloper() == null || detailApts.getDeveloper().trim().isEmpty()) ? null : detailApts.getDeveloper();
        String constructor = (detailApts.getConstructor() == null || detailApts.getConstructor().trim().isEmpty()) ? null : detailApts.getConstructor();
        String buldStru = (apts.getBuldStru() == null || apts.getBuldStru().trim().isEmpty()) ? null : apts.getBuldStru();
        String managementType = (detailApts.getManagementType() == null || detailApts.getManagementType().trim().isEmpty()) ? null : detailApts.getManagementType();
        String heatingType = (detailApts.getHeatingType() == null || detailApts.getHeatingType().trim().isEmpty()) ? null : detailApts.getHeatingType();
        Long cctvCount = (detailApts.getCctvCount() == null || detailApts.getCctvCount() == 0L) ? null : detailApts.getCctvCount();
        Long totalParkingSpaces = (detailApts.getTotalParkingSpaces() == null || detailApts.getTotalParkingSpaces() == 0L) ? null : detailApts.getTotalParkingSpaces();
        String managementOfficeAddress = (detailApts.getManagementOfficeAddress() == null || detailApts.getManagementOfficeAddress().trim().isEmpty()) ? null : detailApts.getManagementOfficeAddress();
        String managementOfficeContact = (detailApts.getManagementOfficeContact() == null || detailApts.getManagementOfficeContact().trim().isEmpty()) ? null : detailApts.getManagementOfficeContact();
        String managementOfficeFax = (detailApts.getManagementOfficeFax() == null || detailApts.getManagementOfficeFax().trim().isEmpty()) ? null : detailApts.getManagementOfficeFax();
        String housingManager = (detailApts.getHousingManager() == null || detailApts.getHousingManager().trim().isEmpty()) ? null : detailApts.getHousingManager();

        return AptInfo.of(
                detailApts.getId(),
                aptNm,
                rdnmadr,
                postalCode,
                completionDate,
                developer,
                constructor,
                numberOfUnits,
                year,
                monthlyMaintenanceData,
                amenitiesList,
                buldStru,
                managementType,
                heatingType,
                cctvCount,
                totalParkingSpaces,
                totalGroundEvChargerCount,
                managementOfficeAddress,
                managementOfficeContact,
                managementOfficeFax,
                housingManager
        );
    }

    // 아파트 정보 상세조회
    @Override
    public AptInfoDetail getApartmentInfoDetail(String detailAptsId) {
        // #1. detailAptsId 를 사용해서 DetailApts의 리포지토리로부터 아파트 상세정보들을 받아온다.
        DetailApts detailApts = detailAptsRepository.findById(detailAptsId)
                .orElseThrow(() -> new RuntimeException("해당 아파트 상세정보가 서버에 존재하지 않습니다."));

        // 전기차 충전 시설 상세 정보를 문자열로 받아옵니다.
        String evChargingDetailsString = (detailApts.getEvChargingFacilitiesDetails() == null || detailApts.getEvChargingFacilitiesDetails().trim().isEmpty())
                ? null : detailApts.getEvChargingFacilitiesDetails();
        // 문자열을 파싱하여 리스트를 채웁니다.
        List<AptInfoDetail.EvChargingFacilityDetail> evChargingFacilitiesDetails = parseEvChargingDetails(evChargingDetailsString);

        // #2. AptInfoDetail DTO 객체를 생성해서 모든 정보를 담는다.
        // null, 0, 빈 문자열 체크 추가
        Long maxFloorCount = (detailApts.getMaxFloorCount() == null || detailApts.getMaxFloorCount() == 0L) ? null : detailApts.getMaxFloorCount();
        Long basementFloorCount = (detailApts.getBasementFloorCount() == null || detailApts.getBasementFloorCount() == 0L) ? null : detailApts.getBasementFloorCount();
        Long passengerCargoElevatorCount = (detailApts.getPassengerCargoElevatorCount() == null || detailApts.getPassengerCargoElevatorCount() == 0L) ? null : detailApts.getPassengerCargoElevatorCount();
        String buildingStructure = (detailApts.getBuildingStructure() == null || detailApts.getBuildingStructure().trim().isEmpty()) ? null : detailApts.getBuildingStructure();
        Boolean groundAccessibleToPublic = detailApts.getGroundAccessibleToPublic(); // Boolean은 0이 없으므로 그대로 유지
        Boolean undergroundAccessibleToPublic = detailApts.getUndergroundAccessibleToPublic();
        Long groundParkingSpaces = (detailApts.getGroundParkingSpaces() == null || detailApts.getGroundParkingSpaces() == 0L) ? null : detailApts.getGroundParkingSpaces();
        Long undergroundParkingSpaces = (detailApts.getUndergroundParkingSpaces() == null || detailApts.getUndergroundParkingSpaces() == 0L) ? null : detailApts.getUndergroundParkingSpaces();
        Long groundEvChargerCount = (detailApts.getGroundEvChargerCount() == null || detailApts.getGroundEvChargerCount() == 0L) ? null : detailApts.getGroundEvChargerCount();
        Long undergroundEvChargerCount = (detailApts.getUndergroundEvChargerCount() == null || detailApts.getUndergroundEvChargerCount() == 0L) ? null : detailApts.getUndergroundEvChargerCount();
        Long groundEvParkingSpaces = (detailApts.getGroundEvParkingSpaces() == null || detailApts.getGroundEvParkingSpaces() == 0L) ? null : detailApts.getGroundEvParkingSpaces();
        Long undergroundEvParkingSpaces = (detailApts.getUndergroundEvParkingSpaces() == null || detailApts.getUndergroundEvParkingSpaces() == 0L) ? null : detailApts.getUndergroundEvParkingSpaces();
        String disinfectionManagementType = (detailApts.getDisinfectionManagementType() == null || detailApts.getDisinfectionManagementType().trim().isEmpty()) ? null : detailApts.getDisinfectionManagementType();
        String disinfectionManagementContractor = (detailApts.getDisinfectionManagementContractor() == null || detailApts.getDisinfectionManagementContractor().trim().isEmpty()) ? null : detailApts.getDisinfectionManagementContractor();
        Long annualDisinfectionFrequency = (detailApts.getAnnualDisinfectionFrequency() == null || detailApts.getAnnualDisinfectionFrequency() == 0L) ? null : detailApts.getAnnualDisinfectionFrequency();
        String securityManagementType = (detailApts.getSecurityManagementType() == null || detailApts.getSecurityManagementType().trim().isEmpty()) ? null : detailApts.getSecurityManagementType();
        String securityManagementContractor = (detailApts.getSecurityManagementContractor() == null || detailApts.getSecurityManagementContractor().trim().isEmpty()) ? null : detailApts.getSecurityManagementContractor();
        Long securityManagementStaff = (detailApts.getSecurityManagementStaff() == null || detailApts.getSecurityManagementStaff() == 0L) ? null : detailApts.getSecurityManagementStaff();
        String cleaningManagementType = (detailApts.getCleaningManagementType() == null || detailApts.getCleaningManagementType().trim().isEmpty()) ? null : detailApts.getCleaningManagementType();
        String cleaningManagementContractor = (detailApts.getCleaningManagementContractor() == null || detailApts.getCleaningManagementContractor().trim().isEmpty()) ? null : detailApts.getCleaningManagementContractor();
        String foodWasteDisposalMethod = (detailApts.getFoodWasteDisposalMethod() == null || detailApts.getFoodWasteDisposalMethod().trim().isEmpty()) ? null : detailApts.getFoodWasteDisposalMethod();
        Long generalManagementStaff = (detailApts.getGeneralManagementStaff() == null || detailApts.getGeneralManagementStaff() == 0L) ? null : detailApts.getGeneralManagementStaff();

        return AptInfoDetail.of(
                maxFloorCount, basementFloorCount, passengerCargoElevatorCount, buildingStructure,
                groundAccessibleToPublic, undergroundAccessibleToPublic,
                groundParkingSpaces, undergroundParkingSpaces,
                groundEvChargerCount, undergroundEvChargerCount, groundEvParkingSpaces, undergroundEvParkingSpaces, evChargingFacilitiesDetails,
                disinfectionManagementType, disinfectionManagementContractor, annualDisinfectionFrequency,
                securityManagementType, securityManagementContractor, securityManagementStaff,
                cleaningManagementType, cleaningManagementContractor, foodWasteDisposalMethod,
                generalManagementStaff
        );
    }

    public static List<AptInfoDetail.EvChargingFacilityDetail> parseEvChargingDetails(String data) {
        if (data == null || data.isEmpty()) {
            return null; // 값이 없으면 null 반환
        }

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
                    Long count = Long.parseLong(parts[4]); // 충전기 대수
                    String provider = parts[5]; // 공급자
                    // 객체 생성 (count가 0이면 null 처리하지 않음, 필요 시 수정 가능)
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
        return details.isEmpty() ? null : details; // 리스트가 비어있으면 null 반환
    }

    // 관리비 상세조회
    @Override
    public MngCostInfo getMngCostInfoDetail(String detailAptsId) {
        // #1. 지정된 아파트 ID에 대한 아파트 상세정보를 가져옵니다.
        DetailApts detailApts = detailAptsRepository.findById(detailAptsId)
                .orElseThrow(() -> new RuntimeException("해당 아파트 상세정보가 서버에 존재하지 않습니다."));

        // #1-2. 세대수 가져오기
        Long numberOfUnits = (detailApts.getNumberOfUnits() == null || detailApts.getNumberOfUnits() == 0L) ? null : detailApts.getNumberOfUnits();

        // #2. 지정된 아파트 ID에 대한 관리비 세부 정보를 가져옵니다.
        List<MngCost> mngCosts = mngCostRepository.findByDetailAptsId(detailAptsId);

        // #2-2. 첫 번째 레코드의 occurrenceYearMonth에서 연도를 추출합니다.
        String year = (mngCosts == null || mngCosts.isEmpty())
                ? null
                : mngCosts.get(0).getOccurrenceYearMonth().substring(0, 4);
        year = (year == null || year.trim().isEmpty()) ? null : year;

        // #2-3. 개별 사용료를 변환하고 월 기준으로 정렬합니다.
        List<MngCostInfo.MonthlyFee> individualUsageSum = (mngCosts == null || mngCosts.isEmpty())
                ? null
                : mngCosts.stream()
                .map(mngCost -> {
                    String occurrenceYearMonth = String.valueOf(mngCost.getOccurrenceYearMonth());
                    String month = occurrenceYearMonth.substring(4, 6);
                    Long individualUsage = (numberOfUnits != null && numberOfUnits > 0 && mngCost.getIndividualUsageSum() != null && mngCost.getIndividualUsageSum() > 0)
                            ? mngCost.getIndividualUsageSum() / numberOfUnits
                            : null;
                    return new MngCostInfo.MonthlyFee(month, individualUsage);
                })
                .sorted(Comparator.comparing(MngCostInfo.MonthlyFee::month)) // 월 기준으로 정렬
                .collect(Collectors.toList());
        // 리스트가 비어있으면 null로 변환
        individualUsageSum = (individualUsageSum == null || individualUsageSum.stream().allMatch(f -> f.fee() == null)) ? null : individualUsageSum;

        // #2-4. 공용 관리비를 변환하고 월 기준으로 정렬합니다.
        List<MngCostInfo.MonthlyFee> totalCommonManagementFeeSum = (mngCosts == null || mngCosts.isEmpty())
                ? null
                : mngCosts.stream()
                .map(mngCost -> {
                    String occurrenceYearMonth = String.valueOf(mngCost.getOccurrenceYearMonth());
                    String month = occurrenceYearMonth.substring(4, 6);
                    Long commonFee = (numberOfUnits != null && numberOfUnits > 0 && mngCost.getTotalCommonManagementFeeSum() != null && mngCost.getTotalCommonManagementFeeSum() > 0)
                            ? mngCost.getTotalCommonManagementFeeSum() / numberOfUnits
                            : null;
                    return new MngCostInfo.MonthlyFee(month, commonFee);
                })
                .sorted(Comparator.comparing(MngCostInfo.MonthlyFee::month)) // 월 기준으로 정렬
                .collect(Collectors.toList());
        // 리스트가 비어있으면 null로 변환
        totalCommonManagementFeeSum = (totalCommonManagementFeeSum == null || totalCommonManagementFeeSum.stream().allMatch(f -> f.fee() == null)) ? null : totalCommonManagementFeeSum;

        // #2-5. 장충금 월 부과액을 변환하고 월 기준으로 정렬합니다.
        List<MngCostInfo.MonthlyFee> reserveFundMonthlyCharge = (mngCosts == null || mngCosts.isEmpty())
                ? null
                : mngCosts.stream()
                .map(mngCost -> {
                    String occurrenceYearMonth = String.valueOf(mngCost.getOccurrenceYearMonth());
                    String month = occurrenceYearMonth.substring(4, 6);
                    Long reserveFund = (numberOfUnits != null && numberOfUnits > 0 && mngCost.getReserveFundMonthlyCharge() != null && mngCost.getReserveFundMonthlyCharge() > 0)
                            ? mngCost.getReserveFundMonthlyCharge() / numberOfUnits
                            : null;
                    return new MngCostInfo.MonthlyFee(month, reserveFund);
                })
                .sorted(Comparator.comparing(MngCostInfo.MonthlyFee::month)) // 월 기준으로 정렬
                .collect(Collectors.toList());
        // 리스트가 비어있으면 null로 변환
        reserveFundMonthlyCharge = (reserveFundMonthlyCharge == null || reserveFundMonthlyCharge.stream().allMatch(f -> f.fee() == null)) ? null : reserveFundMonthlyCharge;

        // #3. MngCostInfo DTO를 빌드하고 반환합니다.
        return MngCostInfo.builder()
                .year(year)
                .individualUsageSum(individualUsageSum)
                .totalCommonManagementFeeSum(totalCommonManagementFeeSum)
                .reserveFundMonthlyCharge(reserveFundMonthlyCharge)
                .build();
    }
}
