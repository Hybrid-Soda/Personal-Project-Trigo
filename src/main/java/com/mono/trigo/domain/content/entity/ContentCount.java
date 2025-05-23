package com.mono.trigo.domain.content.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@BatchSize(size = 50)
@Entity(name = "ContentCount")
public class ContentCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long id;

    @Column(name = "review_count")
    private int reviewCount;

    @Column(name = "average_score")
    private float averageScore;

    // 리뷰수 증가
    public void plusReview(int newScore) {
        this.reviewCount++;
        this.averageScore += (newScore - this.averageScore) / this.reviewCount;
    }

    // 리뷰수 감소
    public void minusReview(int newScore) {
        this.reviewCount--;
        this.averageScore -= (newScore - this.averageScore) / this.reviewCount;
    }

    // 평균 스코어 구하기 (증분 평균 계산)
    public void updateAverageScore(int oldScore, int newScore) {
        this.averageScore += (float) (newScore - oldScore) / this.reviewCount;
    }
}
