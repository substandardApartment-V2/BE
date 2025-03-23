package com.myapt.domain.apt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.myapt.domain.apt.dto.MapMarkerInfo;
import com.myapt.domain.apt.dto.MapSearchInfo;
import com.myapt.domain.apt.repository.AptRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {
	private final AptRepository aptRepository;

	@Override
	public List<MapMarkerInfo> getMapMarkers(String type, double minLa, double minLo, double maxLa, double maxLo) {
		List<MapMarkerInfo> markers;

		// 공백 제거 후 비교
		if ("defect".equalsIgnoreCase(type.trim())) {
			markers = aptRepository.findByIsDefectTrueAndLaBetweenAndLoBetween(minLa, maxLa, minLo, maxLo)
				.stream()
				.map(building -> MapMarkerInfo.of(
					building.getId(),
					building.getAptNm(),
					building.getRdnmadr(),
					building.getLa(),
					building.getLo()))
				.collect(Collectors.toList());
		} else {
			markers = aptRepository.findByLaBetweenAndLoBetween(minLa, maxLa, minLo, maxLo)
				.stream()
				.map(building -> MapMarkerInfo.of(
					building.getId(),
					building.getAptNm(),
					building.getRdnmadr(),
					building.getLa(),
					building.getLo()))
				.collect(Collectors.toList());
		}

		return markers;
	}

	@Override
	public Page<MapSearchInfo> searchApts(String type, String keyword, Pageable pageable) {
		Page<MapSearchInfo> results;

		// 공백 제거 후 비교
		if ("defect".equalsIgnoreCase(type.trim())) {
			results = aptRepository.findByIsDefectTrueAndAptNmContainingOrIsDefectTrueAndRdnmadrContaining(keyword, pageable)
				.map(apt -> MapSearchInfo.of(apt.getId(), apt.getAptNm(), apt.getRdnmadr(), apt.getLa(), apt.getLo()));
		} else {
			results = aptRepository.findByAptNmContainingOrRdnmadrContaining(keyword, pageable)
				.map(apt -> MapSearchInfo.of(apt.getId(), apt.getAptNm(), apt.getRdnmadr(), apt.getLa(), apt.getLo()));
		}

		return results;
	}
}