package com.myapt.domain.apt.dto;

import lombok.Builder;

@Builder
public record MapMarkerInfo(
	String aptId,
	String aptName,
	String aptAddress,
	Double latitude,
	Double longitude
) {
	public static MapMarkerInfo of(String aptId, String aptName, String aptAddress, Double latitude, Double longitude) {
		return MapMarkerInfo.builder()
			.aptId(aptId)
			.aptName(aptName)
			.aptAddress(aptAddress)
			.latitude(latitude)
			.longitude(longitude)
			.build();
	}
}
