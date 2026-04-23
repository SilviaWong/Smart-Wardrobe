package com.smartwardrobe.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartwardrobe.model.dto.AdminDashboardDTO;
import com.smartwardrobe.model.dto.UserAdminDTO;

public interface AdminService {
    AdminDashboardDTO getDashboardStats();
    Page<UserAdminDTO> getUserList(int page, int size, String keyword);
    void toggleUserStatus(Long userId, Boolean isActive);
}
