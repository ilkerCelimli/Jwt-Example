package com.securityexample.example.request;

public record RegisterRequest(
        String username,
        String password,
        String secretKey
) {
}
