package com.securityexample.example.api.v1;

import com.securityexample.example.request.LoginRequest;
import com.securityexample.example.request.RegisterRequest;
import com.securityexample.example.service.UserService;
import com.securityexample.example.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/auth")
@RestController
@RequiredArgsConstructor
public class AuthApi {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    @GetMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {

        userService.loadUserByUsername(loginRequest.username());
        String token = jwtUtil.encodeToken(loginRequest);
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION,token).build();
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest registerRequest) {
        this.userService.save(registerRequest);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/deneme")
    public ResponseEntity<Object> deneme() {
        return ResponseEntity.ok("Success");
    }



}
