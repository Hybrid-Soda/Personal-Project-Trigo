package com.mono.trigo.domain.area.entity;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@BatchSize(size = 50)
@Entity(name = "AreaDetails")
public class AreaDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "area_detail_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String code;
}
