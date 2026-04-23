package com.smartwardrobe.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartwardrobe.model.vo.ApiResponse;
import com.smartwardrobe.model.dto.AdminDashboardDTO;
import com.smartwardrobe.model.dto.UserAdminDTO;
import com.smartwardrobe.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/dashboard")
    public ApiResponse<AdminDashboardDTO> getDashboardStats() {
        return ApiResponse.success(adminService.getDashboardStats());
    }

    @GetMapping("/users")
    public ApiResponse<Page<UserAdminDTO>> getUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.success(adminService.getUserList(page, size, keyword));
    }

    @PutMapping("/users/{id}/status")
    public ApiResponse<Void> toggleUserStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
        Boolean isActive = payload.get("isActive");
        if (isActive == null) {
            return ApiResponse.failure(400, "缺少状态参数");
        }
        adminService.toggleUserStatus(id, isActive);
        return ApiResponse.success(null);
    }
}
