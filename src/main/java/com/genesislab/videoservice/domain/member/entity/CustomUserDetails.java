package com.genesislab.videoservice.domain.member.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CustomUserDetails implements UserDetails {

    private String email;
    private String password;
    private String name;
    private String authority;
    private boolean enabled;

    private CustomUserDetails(String email, String password, String name, String authority) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.authority = authority;
    }

    public static CustomUserDetails of(final Member member) {
        return new CustomUserDetails(member.getEmail().getValue(), "", member.getName().getValue(), member.getRoleKey());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority(authority));
        return auth;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return enabled;
    }
}
