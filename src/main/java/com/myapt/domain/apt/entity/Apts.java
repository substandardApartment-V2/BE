package com.myapt.domain.apt.entity;

import java.util.List;

import com.myapt.domain.defect.entity.DefectApts;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Apts {
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY) // 외부에서 id를 입력받아 옴.
	@Column(name = "apts_id")
	private String id;

	@Column(nullable = false, name = "apt_nm")
	private String aptNm; // 아파트명

	@Column(nullable = false)
	private String rdnmadr; // 도로명주소

	private Integer nmhsh; // 세대수

	@Column(name = "use_aprv_year")
	private Integer useAprvYear; // 사용승인년도

	@Column(nullable = false)
	private Double lo; // 경도

	@Column(nullable = false)
	private Double la; // 위도

	@Column(name = "cnst_entrprs_nm")
	private String cnstEntrprsNm; // 건설업체명

	@Column(name = "hus_mngm_entrprs_nm")
	private String husMngmEntrprsNm; // 주택관리업체명

	private String buldStru; // 건물구조

	//연관관계
	@OneToMany(mappedBy = "apts", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DefectApts> defectApts;

}