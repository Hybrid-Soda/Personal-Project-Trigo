package com.mono.trigo.domain.content.entity;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ContentTypes")
public class ContentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_type_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String cat1;

    @Column(nullable = false, length = 20)
    private String cat2;

    @Column(nullable = false, length = 20)
    private String cat3;

    @Column(nullable = false)
    private String name;

}
