package com.securityexample.example.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securityexample.example.request.LoginRequest;
import com.securityexample.example.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JwtFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtFilter(AuthenticationManager authenticationManager) {


        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            JwtUtil jwtUtil = new JwtUtil();
            byte[] stream = request.getInputStream().readAllBytes();
            ObjectMapper objectMapper = new ObjectMapper();
           LoginRequest loginRequest =  objectMapper.readValue(stream,LoginRequest.class);
           String token = jwtUtil.encodeToken(loginRequest);
           response.addHeader(HttpHeaders.AUTHORIZATION,"Bearer "+token);
           response.setStatus(200);

            List<SimpleGrantedAuthority> roles = new ArrayList<>();
           return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(),null,roles));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.sendError(401,"YEmedi kiiiii.");
    }
}
