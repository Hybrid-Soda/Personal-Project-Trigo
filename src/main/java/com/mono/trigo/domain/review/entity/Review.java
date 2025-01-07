package com.mono.trigo.domain.review.entity;

import com.mono.trigo.common.audit.BaseEntity;
import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.user.entity.User;

import lombok.*;
import jakarta.persistence.*;

import javax.swing.text.html.parser.ContentModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Reviews")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "content_id", nullable = false)
//    private Content content;

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

    @Builder
    public Review(Content content, User user, Integer rating,
                  String reviewContent, String pictureList) {

//        this.content = content;
        this.user = user;
        this.rating = rating;
        this.reviewContent = reviewContent;
        this.pictureList = pictureList;
    }
}
