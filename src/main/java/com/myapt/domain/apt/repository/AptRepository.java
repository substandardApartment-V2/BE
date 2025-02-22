package com.myapt.domain.apt.repository;

import com.myapt.domain.apt.entity.Apts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AptRepository extends JpaRepository<Apts, Long> {
    Optional<Apts> findById(String aptsId);

    @Query("SELECT a FROM Apts a WHERE a.la BETWEEN :minLa AND :maxLa AND a.lo BETWEEN :minLo AND :maxLo")
    List<Apts> findByLaBetweenAndLoBetween(
        @Param("minLa") double minLa,
        @Param("maxLa") double maxLa,
        @Param("minLo") double minLo,
        @Param("maxLo") double maxLo
    );

    @Query("SELECT a FROM Apts a WHERE a.isDefect = true AND a.la BETWEEN :minLa AND :maxLa AND a.lo BETWEEN :minLo AND :maxLo")
    List<Apts> findByIsDefectTrueAndLaBetweenAndLoBetween(
        @Param("minLa") double minLa,
        @Param("maxLa") double maxLa,
        @Param("minLo") double minLo,
        @Param("maxLo") double maxLo
    );

    @Query("SELECT a FROM Apts a WHERE a.isDefect = true AND (a.aptNm LIKE %:keyword% OR a.rdnmadr LIKE %:keyword%)")
    Page<Apts> findByIsDefectTrueAndAptNmContainingOrIsDefectTrueAndRdnmadrContaining(
        @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT a FROM Apts a WHERE a.aptNm LIKE %:keyword% OR a.rdnmadr LIKE %:keyword%")
    Page<Apts> findByAptNmContainingOrRdnmadrContaining(
        @Param("keyword") String keyword, Pageable pageable);
}