package com.troia.libraryproject.model.User;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_MEMBER,
    ROLE_GUEST,
    ROLE_LIBRARIAN,
    ROLE_ADMIN;

    public String getAuthority() {
        return name();
    }

}
