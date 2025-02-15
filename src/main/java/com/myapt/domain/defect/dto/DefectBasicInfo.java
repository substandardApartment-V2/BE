package com.myapt.domain.defect.dto;

import lombok.Builder;

@Builder
public record DefectBasicInfo(
	String completionDate, // 준공일
	Integer numberOfUnits, // 세대수
	String desgnr, // 설계사
	String cnstEntrprs, // 시공사
	String sprvsr // 감리사
) {
	public static DefectBasicInfo of(String completionDate, Integer numberOfUnits, String desgnr, String cnstEntrprs, String sprvsr) {
		return DefectBasicInfo.builder()
			.completionDate(completionDate)
			.numberOfUnits(numberOfUnits)
			.desgnr(desgnr)
			.cnstEntrprs(cnstEntrprs)
			.sprvsr(sprvsr)
			.build();
	}
}
