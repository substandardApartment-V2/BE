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
	public List<MapMarkerInfo> getMapMarkers(double minLa, double minLo, double maxLa, double maxLo) {
		List<MapMarkerInfo> markers = aptRepository.findByLaBetweenAndLoBetween(minLa, maxLa, minLo, maxLo)
			.stream()
			.map(building -> MapMarkerInfo.of(
				building.getId(),
				building.getAptNm(),
				building.getLa(),
				building.getLo()))
			.collect(Collectors.toList());

		if (markers.isEmpty()) {
			throw new MarkerNotFoundException();
		}

		return markers;
	}
}