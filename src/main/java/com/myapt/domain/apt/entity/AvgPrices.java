package com.myapt.domain.apt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AvgPrices {
    @Id
    @Column(name = "avg_price_id")
    private String id; // 평균매매가격 id -- 외부로부터 입력받음

    @Column
    private Long month; // 월

    @Column(name = "avg_price")
    private Long avgPrice; // 평균 가격

}