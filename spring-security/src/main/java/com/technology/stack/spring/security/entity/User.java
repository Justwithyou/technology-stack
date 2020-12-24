package com.technology.stack.spring.security.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 自定义SpringSecurity中的UserDetails
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/23 11:18
 */
public class User implements UserDetails, Serializable {

    private Integer id;
    private String username;
    private String password;
    private boolean enable;
    private boolean locked;
    public List<Role> roleList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roleList) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
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
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }
}
