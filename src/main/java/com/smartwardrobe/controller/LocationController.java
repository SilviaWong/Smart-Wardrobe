package com.smartwardrobe.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartwardrobe.mapper.UserMapper;
import com.smartwardrobe.model.entity.Location;
import com.smartwardrobe.model.entity.User;
import com.smartwardrobe.model.vo.ApiResponse;
import com.smartwardrobe.service.LocationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;
    private final UserMapper userMapper;

    public LocationController(LocationService locationService, UserMapper userMapper) {
        this.locationService = locationService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ApiResponse<List<Location>> list() {
        Long userId = currentUserId();
        return ApiResponse.success(locationService.listLocations(userId));
    }

    @PostMapping
    public ApiResponse<Location> create(@RequestBody Map<String, Object> payload) {
        Long userId = currentUserId();
        String name = (String) payload.get("name");
        Long parentId = payload.get("parentId") != null ? Long.valueOf(payload.get("parentId").toString()) : null;
        return ApiResponse.success(locationService.createLocation(userId, name, parentId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Map<String, String>> delete(@PathVariable Long id) {
        Long userId = currentUserId();
        locationService.deleteLocation(userId, id);
        return ApiResponse.success(Map.of("message", "deleted"));
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
