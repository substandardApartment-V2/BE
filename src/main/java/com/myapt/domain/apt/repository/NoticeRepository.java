package com.myapt.domain.apt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapt.domain.apt.entity.Notices;

@Repository
public interface NoticeRepository extends JpaRepository<Notices, Long> {
	Optional<Notices> findById(Long id);
}
