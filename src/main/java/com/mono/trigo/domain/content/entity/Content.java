package com.mono.trigo.domain.content.entity;

import com.mono.trigo.common.audit.BaseEntity;
import com.mono.trigo.domain.area.entity.AreaDetail;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Contents")
public class Content extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "content_type_id", nullable = false)
    private ContentType contentType;

    @ManyToOne
    @JoinColumn(name = "area_detail_id", nullable = false)
    private AreaDetail areaDetail;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "map_x", nullable = false)
    private Double mapX;

    @Column(name = "map_y", nullable = false)
    private Double mapY;

    @Column(name = "addr1")
    private String addr1;

    @Column(name = "addr2")
    private String addr2;

    @Column(name = "tel")
    private String tel;

    @Column(name = "first_image", length = 512)
    private String firstImage;

    @Column(name = "first_image2", length = 512)
    private String firstImage2;
}
