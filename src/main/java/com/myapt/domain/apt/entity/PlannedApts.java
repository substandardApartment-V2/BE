package com.myapt.domain.apt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PlannedApts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planned_apts_id")
    private int id; // 건설예정아파트 id -- 외부로부터 입력받음

    @Column
    private Long year; // 년

    @Column
    private Long month; // 월

    @Column
    private Long count; // 아파트 갯수
}
