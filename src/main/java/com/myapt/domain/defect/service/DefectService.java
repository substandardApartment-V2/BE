package com.myapt.domain.defect.service;

import java.util.List;

import com.myapt.domain.defect.dto.DefectInfoResponse;
import com.myapt.domain.defect.dto.DefectMainResponse;

public interface DefectService {
	// MainResponseDto getDefectLocs();
	List<DefectMainResponse> getDefectMain();
	DefectInfoResponse getInfoDefectApt(String id);
}