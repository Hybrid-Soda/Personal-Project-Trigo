package com.mono.trigo.domain.user.entity;

import com.mono.trigo.common.audit.BaseEntity;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, length = 10)
    private String birthday;

    @Column(nullable = false)
    private Boolean gender;

    @Column
    private String location;

    @Column(nullable = false, length = 50)
    private String role = "member";

}
