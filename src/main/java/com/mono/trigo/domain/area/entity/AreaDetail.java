package com.mono.trigo.domain.area.entity;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
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

    @Builder
    public AreaDetail(Area area, String name, String code) {
        this.area = area;
        this.name = name;
        this.code = code;
    }
}
