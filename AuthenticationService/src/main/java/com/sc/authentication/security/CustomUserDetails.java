package com.sc.authentication.security;

import com.sc.authentication.model.Roles;
import com.sc.authentication.model.UserInfo;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Collection<? extends GrantedAuthority> grantedAuthorities;

    private final String username;
    private final String password;

    public CustomUserDetails(@NonNull UserInfo userInfo) {
        this.username = userInfo.getUserEmail(); // sets userEmail as the default username for security config.
        this.password = userInfo.getPassword();

        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (Roles role : userInfo.getUserRoles()) {
            authorityList.add(new SimpleGrantedAuthority(role.getRoleName().toUpperCase()));
        }
        this.grantedAuthorities = authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }
}