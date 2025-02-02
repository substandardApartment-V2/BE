package com.myapt.domain.apt.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping("/building")
	public ResTemplate<List<MapMarkerInfo>> getMapMarkers(
		@RequestParam double minLa,
		@RequestParam double minLo,
		@RequestParam double maxLa,
		@RequestParam double maxLo) {
		List<MapMarkerInfo> data = mapService.getMapMarkers(minLa, minLo, maxLa, maxLo);
		return new ResTemplate<>(HttpStatus.OK, "지도 마커 조회 성공", data);
	}
}