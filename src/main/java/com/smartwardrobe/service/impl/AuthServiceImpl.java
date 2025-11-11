package com.smartwardrobe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartwardrobe.mapper.UserMapper;
import com.smartwardrobe.model.dto.LoginRequest;
import com.smartwardrobe.model.dto.RegisterRequest;
import com.smartwardrobe.model.entity.User;
import com.smartwardrobe.model.vo.AuthResponse;
import com.smartwardrobe.security.JwtUtil;
import com.smartwardrobe.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername()));
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponse(token);
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        long existing = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername()));
        if (existing > 0) {
            throw new IllegalArgumentException("Username already exists");
        }
        String email = normalizeOptional(request.getEmail());
        if (email != null) {
            long emailCount = userMapper.selectCount(new LambdaQueryWrapper<User>()
                    .eq(User::getEmail, email));
            if (emailCount > 0) {
                throw new IllegalArgumentException("Email already in use");
            }
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(email);
        user.setGender(normalizeOptional(request.getGender()));
        user.setRegion(normalizeOptional(request.getRegion()));
        user.setStylePreference(normalizeOptional(request.getStylePreference()));
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    private String normalizeOptional(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
