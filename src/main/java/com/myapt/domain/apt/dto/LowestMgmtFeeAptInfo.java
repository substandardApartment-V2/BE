package com.myapt.domain.apt.dto;

import lombok.Builder;

@Builder
public record LowestMgmtFeeAptInfo(
	String address,
	String name
) {
	public static LowestMgmtFeeAptInfo of(String address, String name) {
		return LowestMgmtFeeAptInfo.builder()
			.address(address)
			.name(name)
			.build();
	}
}
