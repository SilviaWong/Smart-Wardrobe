package com.smartwardrobe.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartwardrobe.mapper.UserMapper;
import com.smartwardrobe.model.dto.OutfitDTO;
import com.smartwardrobe.model.entity.Outfit;
import com.smartwardrobe.model.entity.User;
import com.smartwardrobe.model.vo.ApiResponse;
import com.smartwardrobe.service.OutfitService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/outfits")
public class OutfitController {

    private final OutfitService outfitService;
    private final UserMapper userMapper;

    public OutfitController(OutfitService outfitService, UserMapper userMapper) {
        this.outfitService = outfitService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ApiResponse<Map<String, Object>> list(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        Long userId = currentUserId();
        IPage<Outfit> result = outfitService.listOutfits(userId, new Page<>(page, size));
        Map<String, Object> data = new HashMap<>();
        data.put("total", result.getTotal());
        data.put("items", result.getRecords());
        return ApiResponse.success(data);
    }

    @PostMapping
    public ApiResponse<Outfit> create(@Valid @RequestBody OutfitDTO dto) {
        Long userId = currentUserId();
        return ApiResponse.success(outfitService.createOutfit(userId, dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Map<String, String>> delete(@PathVariable Long id) {
        Long userId = currentUserId();
        outfitService.deleteOutfit(userId, id);
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
