package com.myapt.domain.apt.repository;

import com.myapt.domain.apt.entity.PlannedApts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlannedAptsRepository extends JpaRepository<PlannedApts, Integer> {
    // 연도와 월을 기준으로 모든 결과 반환
    List<PlannedApts> findAllByYearAndMonth(Long year, Long month);

    //연도와 월을 기준으로 검색
    Optional<PlannedApts> findByYearAndMonth(Long year, Long month);
}
