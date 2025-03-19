package com.myapt.domain.apt.repository;

import com.myapt.domain.apt.entity.PlannedApts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlannedAptsRepository extends JpaRepository<PlannedApts, Integer> {

    //연도와 월을 기준으로 검색
    Optional<PlannedApts> findByYearAndMonth(Long year, Long month);

    // 최신 데이터 조회 -> 해시값 비교
    Optional<PlannedApts> findTopByOrderByYearDescMonthDesc();
}
