package com.securityexample.example.service;

import com.securityexample.example.request.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

   void save(RegisterRequest registerRequest);
   String handlePasswordEncode(String password);
}
