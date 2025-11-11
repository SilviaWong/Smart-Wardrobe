package com.smartwardrobe.controller;

import com.smartwardrobe.model.dto.LoginRequest;
import com.smartwardrobe.model.dto.RegisterRequest;
import com.smartwardrobe.model.vo.ApiResponse;
import com.smartwardrobe.model.vo.AuthResponse;
import com.smartwardrobe.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }

    @PostMapping("/register")
    public ApiResponse<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ApiResponse.success(Map.of("message", "registered"));
    }
}
