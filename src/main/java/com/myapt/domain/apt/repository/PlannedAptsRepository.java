package com.myapt.domain.apt.repository;

import com.myapt.domain.apt.entity.PlannedApts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedAptsRepository extends JpaRepository<PlannedApts, Integer> {
}
