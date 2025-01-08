package com.mono.trigo.domain.plan.entity;

import com.mono.trigo.common.audit.BaseEntity;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Plans")
public class Plan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

//    @ManyToOne
//    @JoinColumn(name = "area_detail_id", nullable = false)
//    private AreaDetail areaDetail;

    @Column(nullable = false, length = 100)
    private String title;

    @Column
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Lob
    @Column
    private String detail;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;

    @Builder
    public Plan(String title, String description,
                LocalDate startDate, LocalDate endDate, String detail, Boolean isPublic) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.detail = detail;
        this.isPublic = isPublic != null ? isPublic : false;
    }
//        this.user = user;
//        this.areaDetail = areaDetail;
}
