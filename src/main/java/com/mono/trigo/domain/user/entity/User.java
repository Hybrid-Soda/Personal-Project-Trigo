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
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false, length = 50)
    private String role = "member";

    @Builder
    public User(String username, String password, String nickname, LocalDate birthday,
                Gender gender, String role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
        this.role = role != null ? role : "member";
    }

}
