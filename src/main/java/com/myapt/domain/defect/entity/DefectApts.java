package com.myapt.domain.defect.entity;

import com.myapt.domain.apt.entity.Apts;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DefectApts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "defect_apts_id")
	private Long id;

	@Column(nullable = false)
	private String zipcode; // 우편번호

	@Column(nullable = false)
	private String desgnr; // 설계사

	@Column(nullable = false)
	private String sprvsr; // 감리사

	@Column(nullable = false, name = "defect_type")
	private String defectType; // 하자유형

	@Column(nullable = false, name = "reinf_status")
	private String reinfStatus; // 보강상태

	@Column(nullable = false, name = "reinf_method")
	private String reinfContent; // 보강내용

	// 연관관계
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "apts_id")
	private Apts apts;
}
