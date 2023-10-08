package ru.skypro.lessons.springboot.springboot.security;

import org.springframework.security.core.GrantedAuthority;
import ru.skypro.lessons.springboot.springboot.entity.Authority;

public class SecurityGrantedAuthorities implements GrantedAuthority {
    private final String role;

    public SecurityGrantedAuthorities(Authority authority) {
        this.role = authority.getRole();
    }

    @Override
    public String getAuthority() {
        return this.role;
    }
}
