package com.myapt.domain.defect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapt.domain.defect.entity.DefectApts;

@Repository
public interface DefectRepository extends JpaRepository<DefectApts, Long> {
	DefectApts findByAptsId(String id);
}

