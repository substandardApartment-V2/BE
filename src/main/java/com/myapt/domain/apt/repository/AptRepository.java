package com.myapt.domain.apt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapt.domain.apt.entity.Apts;

@Repository
public interface AptRepository extends JpaRepository<Apts, Long> {
}
