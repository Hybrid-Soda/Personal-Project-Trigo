package com.mono.trigo.domain.plan.entity;

import com.mono.trigo.common.audit.BaseEntity;

import com.mono.trigo.domain.area.entity.AreaDetail;
import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.user.entity.User;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "area_detail_id", nullable = false)
    private AreaDetail areaDetail;

    @OneToMany
    @JoinColumn(name = "plan_id")
    private List<Content> contents = new ArrayList<>();

    @Column(nullable = false, length = 100)
    private String title;

    @Column
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;

    @Builder
    public Plan(User user, AreaDetail areaDetail, String title, String description,
                LocalDate startDate, LocalDate endDate, List<Content> contents, Boolean isPublic) {
        this.user = user;
        this.areaDetail = areaDetail;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contents = contents;
        this.isPublic = isPublic != null ? isPublic : false;
    }
}
