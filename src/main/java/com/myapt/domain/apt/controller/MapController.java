package com.myapt.domain.apt.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.myapt.domain.apt.dto.MapMarkerInfo;
import com.myapt.domain.apt.service.MapService;
import com.myapt.global.template.ResTemplate;

@RestController
@RequestMapping("/map")
public class MapController {
	private final MapService mapService;

	public MapController(MapService mapService) {
		this.mapService = mapService;
	}

	@GetMapping("/{type}")
	public ResTemplate<List<MapMarkerInfo>> getMapMarkers(
		@PathVariable String type,
		@RequestParam double minLa,
		@RequestParam double minLo,
		@RequestParam double maxLa,
		@RequestParam double maxLo) {
		List<MapMarkerInfo> data = mapService.getMapMarkers(type.trim(), minLa, minLo, maxLa, maxLo);
		return new ResTemplate<>(HttpStatus.OK, "지도 마커 조회 성공", data);
	}

	@GetMapping("/search/{type}")
	public ResTemplate<List<MapMarkerInfo>> searchApts(
		@PathVariable String type,
		@RequestParam String keyword) {
		try {
			List<MapMarkerInfo> data = mapService.searchApts(type.trim(), keyword);
			return new ResTemplate<>(HttpStatus.OK, "아파트 검색 성공", data);
		} catch (ResponseStatusException e) {
			if (e.getStatusCode() == HttpStatus.NO_CONTENT) {
				return new ResTemplate<>(HttpStatus.NO_CONTENT, "검색결과가 없습니다", null);
			}
			throw e;
		}
	}
}