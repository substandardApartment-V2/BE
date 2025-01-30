package com.myapt.domain.defect.dto;

import lombok.Builder;

@Builder
public record DefectAptInfo(
	String name, //아파트 명
	String address, // 주소
	String zipCode, // 우편번호
	String desgnr, // 설계사
	String cnstEntrprs, // 시공사
	String sprvsr // 감리사
) {
	public static DefectAptInfo of(String name, String address, String zipCode, String desgnr, String cnstEntrprs, String sprvsr) {
		return DefectAptInfo.builder()
			.name(name)
			.address(address)
			.zipCode(zipCode)
			.desgnr(desgnr)
			.cnstEntrprs(cnstEntrprs)
			.sprvsr(sprvsr)
			.build();
	}
}
