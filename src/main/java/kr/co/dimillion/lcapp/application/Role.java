package kr.co.dimillion.lcapp.application;

import lombok.Getter;

@Getter
public enum Role {
    WORKER("ROLE_WORKER"), ADMIN("ROLE_ADMIN");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }
}
