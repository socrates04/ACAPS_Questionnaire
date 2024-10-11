package com.example.questionnaire.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class AppUser implements UserDetails {
    @NotNull
    private Long userId;

    @NotEmpty
    private String username;
    @Email @NotEmpty
    private String email;

    private Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

    private boolean enabled;

    private Date creationDate;
    public AppUser(String username, String email) {
        this.username = username;
        this.email = email;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
