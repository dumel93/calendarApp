package com.calendar.app.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Roles {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");
    private final String roleName;

}
