package com.myapt.domain.apt.dto;

import lombok.Builder;

@Builder
public record MapMarkerInfo(
	String buildingId,
	String buildingName,
	Double latitude,
	Double longitude
) {
	public static MapMarkerInfo of(String buildingId, String buildingName, Double latitude, Double longitude) {
		return MapMarkerInfo.builder()
			.buildingId(buildingId)
			.buildingName(buildingName)
			.latitude(latitude)
			.longitude(longitude)
			.build();
	}
}
