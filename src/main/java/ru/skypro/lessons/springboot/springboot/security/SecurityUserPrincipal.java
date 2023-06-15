package ru.skypro.lessons.springboot.springboot.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.lessons.springboot.springboot.entity.AuthUser;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityUserPrincipal implements UserDetails {
    private AuthUser authUser;
    private List<SecurityGrantedAuthorities> authoritiesList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(authoritiesList);
    }
    public SecurityUserPrincipal (AuthUser authUser) {
        this.authUser = authUser;
        this.authoritiesList = authUser.getAuthorityList().stream()
                .map(SecurityGrantedAuthorities::new)
                .toList();
    }

    @Override
    public String getPassword() {
        return authUser.getPassword();
    }

    @Override
    public String getUsername() {
        return authUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return authUser.isEnabled();
    }
}
