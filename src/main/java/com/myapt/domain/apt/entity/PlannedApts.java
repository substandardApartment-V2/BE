package com.myapt.domain.apt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = @Index(columnList = "year,month"))
public class PlannedApts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planned_apts_id")
    private Integer id; // 건설예정아파트 id

    @Column(nullable = false)
    private Long year; // 년

    @Column(nullable = false)
    private Long month; // 월

    @Column(nullable = false)
    private Long count; // 아파트 갯수

    @Column(name = "data_hash", nullable = false)
    private String dataHash; // 데이터 해시값
}
