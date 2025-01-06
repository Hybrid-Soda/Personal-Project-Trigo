package com.mono.trigo.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {

    FEMALE("female"),
    MALE("male");

    private final String gender;

}
