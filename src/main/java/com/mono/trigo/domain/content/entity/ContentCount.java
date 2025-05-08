package com.mono.trigo.domain.content.entity;

import jakarta.persistence.*;

@Entity(name = "ContentCount")
public class ContentCount {

    @Id
    @Column(name = "content_id")
    private Long contentId;

    @Column(name = "review_count")
    private int reviewCount;

    @Column(name = "average_score")
    private float averageScore;

    // 리뷰수 증가
    public void plusReview(int newScore) {
        this.reviewCount++;
        getAverageScore(newScore);
    }

    // 리뷰수 감소
    public void minusReview(int newScore) {
        this.reviewCount++;
        getAverageScore(newScore);
    }

    // 평균 스코어 구하기 (증분 평균 계산)
    public void getAverageScore(int newScore) {
        this.averageScore += (newScore - this.averageScore) / this.reviewCount;
    }
}
