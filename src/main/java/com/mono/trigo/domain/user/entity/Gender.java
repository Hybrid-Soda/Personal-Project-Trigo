package com.mono.trigo.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {

    MALE("male"),
    FEMALE("female");

    private final String gender;

}
