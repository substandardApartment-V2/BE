package com.myapt.domain.apt.dto;

import lombok.Builder;

@Builder
public record MapMarkerInfo(
	String aptId,
	String aptName,
	Double latitude,
	Double longitude
) {
	public static MapMarkerInfo of(String aptId, String aptName, Double latitude, Double longitude) {
		return MapMarkerInfo.builder()
			.aptId(aptId)
			.aptName(aptName)
			.latitude(latitude)
			.longitude(longitude)
			.build();
	}
}
