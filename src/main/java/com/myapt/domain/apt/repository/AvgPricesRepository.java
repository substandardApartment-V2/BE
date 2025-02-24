package com.myapt.domain.apt.repository;

import com.myapt.domain.apt.entity.AvgPrices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvgPricesRepository extends JpaRepository<AvgPrices, String> {
    //getMainInfo 메소드에서 현재 시간을 기준으로 '월'을 가져와서, 해당 월에 해당하는 평균 매매가를 AvgPricesRepository를 통해 조회하고, 이를 aptAvgPrice에 대입하는 코드
    Optional<AvgPrices> findByMonth(Long month);
}
