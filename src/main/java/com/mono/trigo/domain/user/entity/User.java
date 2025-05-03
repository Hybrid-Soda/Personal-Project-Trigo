package com.mono.trigo.domain.user.entity;

import com.mono.trigo.common.audit.BaseEntity;

import com.mono.trigo.domain.like.entity.Like;
import com.mono.trigo.web.user.dto.SignupRequest;
import com.mono.trigo.web.user.dto.UserRequest;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
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

    @Builder.Default
    @Column(nullable = false, length = 50)
    private String role = "member";

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    public static User of(SignupRequest signupRequest, String encodedPassword) {
        String role = signupRequest.getRole();

        return builder()
                .username(signupRequest.getUsername())
                .password(encodedPassword)
                .nickname(signupRequest.getNickname())
                .birthday(signupRequest.getBirthday())
                .gender(signupRequest.getGender())
                .role(role != null ? role : "member")
                .build();
    }

    public void update(UserRequest userRequest) {
        this.setNickname(userRequest.getNickname());
        this.setBirthday(userRequest.getBirthday());
        this.setGender(userRequest.getGender());
    }
}
