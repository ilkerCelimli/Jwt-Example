package com.securityexample.example.service.impl;

import com.securityexample.example.entity.UserEntity;
import com.securityexample.example.repository.UserRepository;
import com.securityexample.example.request.RegisterRequest;
import com.securityexample.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = this.userRepository.findByUsernameEquals(username);
        if(user.isPresent()) {
            UserEntity u = user.get();
            List<SimpleGrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority("USER"));
            return new User(u.getUsername(),u.getPassword(),roles);
        }
        else throw new UsernameNotFoundException("Bulunamadı");


    }

    @Override
    public void save(RegisterRequest registerRequest) {
        UserEntity user = new UserEntity();
        user.setUsername(registerRequest.username());
        user.setPassword(handlePasswordEncode(registerRequest.password()));
        user.setSecretKey(registerRequest.secretKey());
        this.userRepository.save(user);
    }

    @Override
    public String handlePasswordEncode(String password) {
        return passwordEncoder.encode(password);
    }


}
