package com.mono.trigo.domain.content.entity;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@NoArgsConstructor
@BatchSize(size = 50)
@Entity(name = "ContentTypes")
public class ContentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_type_id")
    private Long id;

    @Column(name = "parent_code", length = 10)
    private String parentCode;

    @Column(nullable = false, length = 10)
    private String code;

    @Column(nullable = false, length = 50)
    private String name;

    @Builder
    public ContentType(String parentCode, String code, String name) {
        this.parentCode = parentCode;
        this.code = code;
        this.name = name;
    }
}
