package com.mono.trigo.common;

import lombok.Getter;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter
@MappedSuperclass // 다른 엔티티 클래스에서 상속 가능하게 함
@EntityListeners(AuditingEntityListener.class) // 생성 및 수정 시간을 자동으로 관리
public abstract class BaseTime {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

}
