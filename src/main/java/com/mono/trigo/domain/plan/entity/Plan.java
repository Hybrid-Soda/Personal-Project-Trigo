package com.mono.trigo.domain.plan.entity;

import com.mono.trigo.common.audit.BaseEntity;
import com.mono.trigo.domain.user.entity.User;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Plans")
public class Plan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Lob
    @Column
    private String detail;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;

}
