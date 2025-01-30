package com.myapt.domain.apt.repository;

import com.myapt.domain.apt.entity.Apts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AptRepository extends JpaRepository<Apts, Long> {
    Optional<Apts> findById(String aptsId);
}
