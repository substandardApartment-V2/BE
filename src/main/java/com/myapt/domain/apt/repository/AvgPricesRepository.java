package com.myapt.domain.apt.repository;

import com.myapt.domain.apt.entity.AvgPrices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsvRepository extends JpaRepository<AvgPrices, String> {
}
