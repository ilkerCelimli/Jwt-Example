package com.securityexample.example.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securityexample.example.request.LoginRequest;
import com.securityexample.example.service.UserService;
import com.securityexample.example.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@RequiredArgsConstructor
@Component
public class AuthEntryPoint extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /***
         * Eğer Kayıt olunacaksa hiç bir şey yapmadan geç diyoruz.
         */
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(Objects.isNull(header) && request.getServletPath().contains("/register")) {
            filterChain.doFilter(request,response);
            return;

        }

        if(request.getServletPath().contains("/login")) {
            ObjectMapper o = new ObjectMapper();
            byte[] stream = request.getInputStream().readAllBytes();
            LoginRequest loginRequest = o.readValue(stream,LoginRequest.class);
            UserDetails userDetails = userService.loadUserByUsername(loginRequest.username());
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities()));
            String token = jwtUtil.encodeToken(loginRequest);
            response.addHeader(HttpHeaders.AUTHORIZATION,"Bearer "+token);
            response.setStatus(200);
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request,response);
                 return;
        }

        /***
         * Eğer Token varsa ve gelen token'in süresi bitmiş ise yenileme tokeni burda üretilir..
         */
        if( !Objects.isNull(header) && jwtUtil.isExpiredToken(header)) {
            header = header.substring(7);
           String newToken = jwtUtil.encodeToken(header);
           response.setHeader(HttpHeaders.AUTHORIZATION,newToken);
           Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
           if(Objects.isNull(auth)) {
              String username =  jwtUtil.decodeJwt(header).getClaim("username").asString();
               List<SimpleGrantedAuthority> roles = new ArrayList<>();
               roles.add(new SimpleGrantedAuthority("USER"));
               UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(username,null,null);
               SecurityContextHolder.getContext().setAuthentication(newAuth);
               response.setHeader(HttpHeaders.AUTHORIZATION,"Bearer "+newToken);
               filterChain.doFilter(request,response);
               return;
           }
           auth.setAuthenticated(true);
           SecurityContextHolder.getContext().setAuthentication(auth);
           filterChain.doFilter(request,response);
        }
        response.sendError(401,"Yemedii...");
    }
}
