package com.myapt.domain.apt.repository;

import com.myapt.domain.apt.entity.DetailApts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetailAptsRepository extends JpaRepository<DetailApts, String> {
    // apts.id (즉, apts_id)에 해당하는 DetailApts 객체를 찾는 메서드
    Optional<DetailApts> findByApts_Id(String aptsId);
}
