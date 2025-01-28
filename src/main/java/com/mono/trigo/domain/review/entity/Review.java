package com.mono.trigo.domain.review.entity;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.common.audit.BaseEntity;
import com.mono.trigo.domain.content.entity.Content;

import com.mono.trigo.web.review.dto.ReviewRequest;
import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Reviews")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "review_content")
    private String reviewContent;

    @Lob
    @Column(name = "picture_list")
    private String pictureList;

    public static Review of(ReviewRequest reviewRequest, Content content, User user) {
        return builder()
                .content(content)
                .user(user)
                .rating(reviewRequest.getRating())
                .reviewContent(reviewRequest.getReviewContent())
                .pictureList(reviewRequest.getPictureList())
                .build();
    }
}
