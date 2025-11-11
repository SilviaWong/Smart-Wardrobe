package com.smartwardrobe.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartwardrobe.mapper.UserMapper;
import com.smartwardrobe.model.dto.ClothingDTO;
import com.smartwardrobe.model.entity.Clothes;
import com.smartwardrobe.model.entity.User;
import com.smartwardrobe.model.vo.ApiResponse;
import com.smartwardrobe.service.ClothesService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/clothes")
public class ClothesController {

    private final ClothesService clothesService;
    private final UserMapper userMapper;

    public ClothesController(ClothesService clothesService, UserMapper userMapper) {
        this.clothesService = clothesService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ApiResponse<Map<String, Object>> list(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        Long userId = currentUserId();
        IPage<Clothes> result = clothesService.listClothes(userId, new Page<>(page, size));
        Map<String, Object> data = new HashMap<>();
        data.put("total", result.getTotal());
        data.put("items", result.getRecords());
        return ApiResponse.success(data);
    }

    @PostMapping
    public ApiResponse<Clothes> create(@Valid @RequestBody ClothingDTO dto) {
        Long userId = currentUserId();
        return ApiResponse.success(clothesService.createClothes(userId, dto));
    }

    @PutMapping("/{id}")
    public ApiResponse<Clothes> update(@PathVariable Long id, @Valid @RequestBody ClothingDTO dto) {
        Long userId = currentUserId();
        return ApiResponse.success(clothesService.updateClothes(userId, id, dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Map<String, String>> delete(@PathVariable Long id) {
        Long userId = currentUserId();
        clothesService.deleteClothes(userId, id);
        return ApiResponse.success(Map.of("message", "deleted"));
    }

    private Long currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
        if (user == null) {
            throw new IllegalStateException("User not found");
        }
        return user.getId();
    }
}
