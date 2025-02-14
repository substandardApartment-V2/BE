package com.myapt.domain.apt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.myapt.domain.apt.dto.MapMarkerInfo;
import com.myapt.domain.apt.exception.MarkerNotFoundException;
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
		if ("defect".equals(type.trim().toLowerCase())) {
			markers = aptRepository.findByIsDefectTrueAndLaBetweenAndLoBetween(minLa, maxLa, minLo, maxLo)
				.stream()
				.map(building -> MapMarkerInfo.of(
					building.getId(),
					building.getAptNm(),
					building.getLa(),
					building.getLo()))
				.collect(Collectors.toList());
		} else {
			markers = aptRepository.findByLaBetweenAndLoBetween(minLa, maxLa, minLo, maxLo)
				.stream()
				.map(building -> MapMarkerInfo.of(
					building.getId(),
					building.getAptNm(),
					building.getLa(),
					building.getLo()))
				.collect(Collectors.toList());
		}

		if (markers.isEmpty()) {
			throw new MarkerNotFoundException();
		}

		return markers;
	}
}