package com.mono.trigo.domain.like.entity;

import com.mono.trigo.common.audit.BaseEntity;
import com.mono.trigo.domain.plan.entity.Plan;
import com.mono.trigo.domain.user.entity.User;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "likes", uniqueConstraints = @UniqueConstraint(
        name = "like_uid", columnNames = {"user_id", "plan_id"}))
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Builder
    public Like(User user, Plan plan) {
        this.user = user;
        this.plan = plan;
    }

    public Like() {
    }
}
