package com.myapt.domain.defect.dto;

import lombok.Builder;

@Builder
public record DefectAptInfo(
	String name, //아파트 명
	String address, // 주소
	String zipCode // 우편번호
) {
	public static DefectAptInfo of(String name, String address, String zipCode) {
		return DefectAptInfo.builder()
			.name(name)
			.address(address)
			.zipCode(zipCode)
			.build();
	}
}
