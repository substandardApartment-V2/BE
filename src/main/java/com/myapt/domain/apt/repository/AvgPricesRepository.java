package com.myapt.domain.apt.repository;

import com.myapt.domain.apt.entity.AvgPrices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvgPricesRepository extends JpaRepository<AvgPrices, Long> {
    //현재 시간을 기준으로  '연', '월'을 가져와서, 해당 월에 해당하는 평균 매매가를 찾음
    Optional<AvgPrices> findByYearAndMonth(Long year, Long month);

    //최신 데이터 조회 -> 해시값
    Optional<AvgPrices> findTopByOrderByYearDescMonthDesc();
}
