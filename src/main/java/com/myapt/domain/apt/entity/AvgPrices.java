package com.myapt.domain.apt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = @Index(columnList = "year,month"))
public class AvgPrices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avg_price_id")
    private Long id;

    @Column(nullable = false)
    private Long year;

    @Column(nullable = false)
    private Long month;

    @Column(name = "avg_price", nullable = false)
    private Long avgPrice;

    @Column(name = "date_list_hash", nullable = false)
    private String dateListHash;
}