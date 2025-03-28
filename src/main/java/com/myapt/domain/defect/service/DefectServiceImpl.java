package com.myapt.domain.defect.service;

import java.util.List;

import com.myapt.domain.defect.exception.DefectAptNotFoundException;
import org.springframework.stereotype.Service;

import com.myapt.domain.apt.entity.Apts;
import com.myapt.domain.apt.repository.AptRepository;
import com.myapt.domain.defect.dto.DefectAptInfo;
import com.myapt.domain.defect.dto.DefectBasicInfo;
import com.myapt.domain.defect.dto.DefectBuildInfo;
import com.myapt.domain.defect.dto.DefectInfoResponse;
import com.myapt.domain.defect.dto.DefectMainResponse;
import com.myapt.domain.defect.dto.DefectSplmnInfo;
import com.myapt.domain.defect.entity.DefectApts;
import com.myapt.domain.defect.exception.AptNotFoundException;
import com.myapt.domain.defect.repository.DefectRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefectServiceImpl implements DefectService {
	private final DefectRepository defectRepository;
	private final AptRepository aptRepository;

	@Override
	@Transactional
	public List<DefectMainResponse> getDefectMain() {
		// 최근 뉴스 3개 들고오는 내용.
		return null;
	}

	@Transactional
	public DefectInfoResponse getInfoDefectApt(String id) {
		DefectApts defectApt = defectRepository.findByAptsId(id); // id에 해당하는 defectApt 찾기
		if (defectApt == null) {
			throw DefectAptNotFoundException.defectInfoNotFound(); // id에 해당하는 defectApt가 없으면 AptNotFoundException 발생
		}

		Apts apt = defectApt.getApts();
		if (apt == null) {
			throw DefectAptNotFoundException.aptInfoNotFound(); // apt가 null일 경우 예외 처리
		}

		// 건물 정보 생성
		DefectAptInfo aptInfo = DefectAptInfo.of(
			apt.getAptNm() != null ? apt.getAptNm() : null,
			apt.getRdnmadr() != null ? apt.getRdnmadr() : null,
			defectApt.getZipcode() != null ? defectApt.getZipcode() : null
		);

		// 기본 정보 생성
		DefectBasicInfo basicInfo = DefectBasicInfo.of(
			apt.getUseAprvYear() != null ? String.valueOf(apt.getUseAprvYear()) : null,
			apt.getNmhsh() != null ? apt.getNmhsh() : null,
			defectApt.getDesgnr() != null ? defectApt.getDesgnr() : null,
			apt.getCnstEntrprsNm() != null ? apt.getCnstEntrprsNm() : null,
			defectApt.getSprvsr() != null ? defectApt.getSprvsr() : null
		);

		// 건물 구조, 부실 사유 생성
		DefectBuildInfo buildInfo = DefectBuildInfo.of(
			apt.getBuldStru() != null ? apt.getBuldStru() : null,
			defectApt.getDefectType() != null ? defectApt.getDefectType() : null
		);

		// 보강 상태, 보강 내용 생성
		DefectSplmnInfo splmnInfo = DefectSplmnInfo.of(
			defectApt.getReinfContent() != null ? defectApt.getReinfContent() : null,
			defectApt.getReinfStatus() != null ? defectApt.getReinfStatus() : null
		);

		return DefectInfoResponse.of(aptInfo, basicInfo, buildInfo, splmnInfo);
	}
}
