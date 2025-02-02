package com.backend.farmon.dto.user;

import com.backend.farmon.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override // 유저의 권한(Role)을 반환 해주는 메소드
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());
        collection.add(authority);

        return collection;
    }

    @Override  // 유저의 비밀번호 반환
    public String getPassword() {

        return user.getPassword();
    }

    @Override  // 유저의 이름(이메일) 반환
    public String getUsername() {

        return user.getEmail();
    }

    @Override // 계정 만료 여부
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override // 계정 잠금 여부
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override // 자격증명 만료 여부
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override // 사용자 활성화 여부
    public boolean isEnabled() {

        return true;
    }
}