package com.securityexample.example.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;


public class AuthProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        authentication.setAuthenticated(true);
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        return new UsernamePasswordAuthenticationToken(authentication.getName(),null,list);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
