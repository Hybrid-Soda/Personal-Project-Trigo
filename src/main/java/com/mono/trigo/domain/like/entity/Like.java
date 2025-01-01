package com.mono.trigo.domain.like.entity;

import com.mono.trigo.common.audit.BaseEntity;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Likes")
public class Like extends BaseEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "plan_id", nullable = false)
    private Long planId;

}
