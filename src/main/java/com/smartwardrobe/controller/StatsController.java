package com.smartwardrobe.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartwardrobe.mapper.UserMapper;
import com.smartwardrobe.model.entity.User;
import com.smartwardrobe.model.vo.ApiResponse;
import com.smartwardrobe.model.vo.StatsResponse;
import com.smartwardrobe.service.StatsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;
    private final UserMapper userMapper;

    public StatsController(StatsService statsService, UserMapper userMapper) {
        this.statsService = statsService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ApiResponse<StatsResponse> stats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
        if (user == null) {
            throw new IllegalStateException("User not found");
        }
        return ApiResponse.success(statsService.summarize(user.getId()));
    }
}
