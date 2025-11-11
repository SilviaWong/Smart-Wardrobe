package com.smartwardrobe.service;

import com.smartwardrobe.model.dto.LoginRequest;
import com.smartwardrobe.model.dto.RegisterRequest;
import com.smartwardrobe.model.vo.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    void register(RegisterRequest request);
}
