package com.mono.trigo.domain.review.entity;

import com.mono.trigo.common.audit.BaseEntity;
import com.mono.trigo.domain.spot.entity.Spot;
import com.mono.trigo.domain.user.entity.User;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Reviews")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer rating;

    @Column
    private String content;

    @Lob
    @Column(name = "picture_list")
    private String pictureList;

}
