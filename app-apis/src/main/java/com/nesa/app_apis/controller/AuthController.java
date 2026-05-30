package com.nesa.app_apis.controller;

import com.nesa.app_apis.dto.AuthResponse;
import com.nesa.app_apis.dto.LoginRequest;
import com.nesa.app_apis.dto.RegisterRequest;
import com.nesa.app_apis.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/verify")
    public String verify(@RequestParam String token) {
        return authService.verifyAccount(token);
    }
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody Map<String, String> body) {
        return authService.forgotPassword(body.get("email"));
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String token,
            @RequestBody Map<String, String> body
    ) {
        return authService.resetPassword(token, body.get("password"));
    }
}