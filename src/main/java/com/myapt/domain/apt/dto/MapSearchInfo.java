package com.myapt.domain.apt.dto;

import lombok.Builder;

@Builder
public record MapSearchInfo(
	String aptId,
	String aptName,
	String aptAddress,
	Double latitude,
	Double longitude
) {
	public static MapSearchInfo of(String aptId, String aptName, String aptAddress, Double latitude, Double longitude) {
		return MapSearchInfo.builder()
			.aptId(aptId)
			.aptName(aptName)
			.aptAddress(aptAddress)
			.latitude(latitude)
			.longitude(longitude)
			.build();
	}
}
