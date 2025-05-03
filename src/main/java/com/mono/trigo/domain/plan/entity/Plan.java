package com.mono.trigo.domain.plan.entity;

import com.mono.trigo.common.audit.BaseEntity;

import com.mono.trigo.domain.like.entity.Like;
import com.mono.trigo.web.plan.dto.PlanRequest;
import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.area.entity.AreaDetail;
import com.mono.trigo.domain.content.entity.Content;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Plans")
public class Plan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_detail_id", nullable = false)
    private AreaDetail areaDetail;

    @Builder.Default
    @BatchSize(size = 200)
    @JoinTable(
            name = "plan_content",
            joinColumns = @JoinColumn(name = "plan_id"),
            inverseJoinColumns = @JoinColumn(name = "content_id"),
            uniqueConstraints = @UniqueConstraint(
                name = "plan_content_id", columnNames = {"plan_id", "content_id"}
            )
    )
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Content> contents = new ArrayList<>();

    @Column(nullable = false, length = 100)
    private String title;

    @Column
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Builder.Default
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;

    @Builder.Default
    @OneToMany(mappedBy = "plan", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    public static Plan of(PlanRequest planRequest, User user, AreaDetail areaDetail, List<Content> contents) {
        return builder()
                .user(user)
                .areaDetail(areaDetail)
                .contents(contents)
                .title(planRequest.getTitle())
                .description(planRequest.getDescription())
                .startDate(planRequest.getStartDate())
                .endDate(planRequest.getEndDate())
                .isPublic(planRequest.getIsPublic())
                .build();
    }

    public void update(AreaDetail areaDetail, PlanRequest planRequest, List<Content> contents) {
        this.setAreaDetail(areaDetail);
        this.setTitle(planRequest.getTitle());
        this.setDescription(planRequest.getDescription());
        this.setStartDate(planRequest.getStartDate());
        this.setEndDate(planRequest.getEndDate());
        this.setIsPublic(planRequest.getIsPublic());
        this.setContents(contents);
    }
}
