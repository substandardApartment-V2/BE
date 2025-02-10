package com.myapt.domain.apt.service;

import java.util.List;

import com.myapt.domain.apt.dto.MapMarkerInfo;

public interface MapService {
	public List<MapMarkerInfo> getMapMarkers(double minLa, double minLo, double maxLa, double maxLo);
}
