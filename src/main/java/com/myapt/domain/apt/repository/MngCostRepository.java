package com.myapt.domain.apt.repository;

import com.myapt.domain.apt.entity.MngCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MngCostRepository extends JpaRepository<MngCost, String> {
    List<MngCost> findByDetailAptsId(String id);
}
