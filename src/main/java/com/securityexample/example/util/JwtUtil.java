package com.securityexample.example.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.securityexample.example.request.LoginRequest;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component

public class JwtUtil {
    private final  String secretKey = "MySuperSecrett";

    private final  int expireMin = 30;
    private final Algorithm algorithm;

    public JwtUtil() {
        algorithm = Algorithm.HMAC256(secretKey.getBytes());

    }


    public String encodeToken(LoginRequest loginRequest) {
        return   JWT
                .create()
                .withExpiresAt(new Date(System.currentTimeMillis()*1000*60*expireMin))
                .withClaim("username",loginRequest.username())
                .sign(algorithm);
    }

    public String encodeToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return   JWT
                .create()
                .withExpiresAt(new Date(System.currentTimeMillis()*1000*60*expireMin))
                .withClaim("username",jwt.getClaim("username").asString())
                .sign(algorithm);
    }

    public boolean isExpiredToken(String token){
        token = token.substring(7);
        return JWT.decode(token).getExpiresAt().after(new Date());
    }

    public DecodedJWT decodeJwt(String token) {
        return JWT.decode(token);
    }


}
