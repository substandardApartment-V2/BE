package com.myapt.domain.apt.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.myapt.domain.apt.dto.MapMarkerInfo;
import com.myapt.domain.apt.dto.MapSearchInfo;

public interface MapService {
	List<MapMarkerInfo> getMapMarkers(String type, double minLa, double minLo, double maxLa, double maxLo);
	Page<MapSearchInfo> searchApts(String type, String keyword, Pageable pageable);
}
