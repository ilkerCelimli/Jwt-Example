package com.securityexample.example.request;

public record LoginRequest(
        String username,
        String password
) {
}
