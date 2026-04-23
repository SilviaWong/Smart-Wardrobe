package com.smartwardrobe.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartwardrobe.mapper.UserMapper;
import com.smartwardrobe.model.entity.User;
import com.smartwardrobe.model.entity.WearLog;
import com.smartwardrobe.model.vo.ApiResponse;
import com.smartwardrobe.service.WearLogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wears")
public class WearLogController {

    private final WearLogService wearLogService;
    private final UserMapper userMapper;

    public WearLogController(WearLogService wearLogService, UserMapper userMapper) {
        this.wearLogService = wearLogService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ApiResponse<List<WearLog>> list(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        Long userId = currentUserId();
        return ApiResponse.success(wearLogService.listLogs(userId, from, to));
    }

    @PostMapping
    public ApiResponse<Map<String, String>> log(@RequestBody Map<String, Object> payload) {
        Long userId = currentUserId();
        LocalDate date = payload.get("date") != null ? LocalDate.parse(payload.get("date").toString()) : LocalDate.now();
        Long itemId = payload.get("itemId") != null ? Long.valueOf(payload.get("itemId").toString()) : null;
        Long outfitId = payload.get("outfitId") != null ? Long.valueOf(payload.get("outfitId").toString()) : null;
        
        wearLogService.logWear(userId, date, itemId, outfitId);
        return ApiResponse.success(Map.of("status", "logged"));
    }

    private Long currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return 1L;
        }
        String username = authentication.getName();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
        if (user == null) {
            throw new IllegalStateException("User not found");
        }
        return user.getId();
    }
}
