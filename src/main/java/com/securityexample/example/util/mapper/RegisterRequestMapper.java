package com.securityexample.example.util.mapper;

import com.securityexample.example.entity.UserEntity;
import com.securityexample.example.request.RegisterRequest;
import org.springframework.stereotype.Component;

@Component
public class RegisterRequestMapper {

    public UserEntity toEntity(RegisterRequest req) {
        return new UserEntity(null,req.username(),req.password(), req.secretKey());
    }




}
