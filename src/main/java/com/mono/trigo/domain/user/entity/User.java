package com.mono.trigo.domain.user.entity;

import com.mono.trigo.common.audit.BaseEntity;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false)
    private Gender gender;

    @Column
    private String location;

    @Column(nullable = false, length = 50)
    private String role = "member";

}
