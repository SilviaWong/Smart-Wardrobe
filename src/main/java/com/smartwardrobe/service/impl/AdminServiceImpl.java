package com.smartwardrobe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartwardrobe.mapper.ClothesMapper;
import com.smartwardrobe.mapper.UserMapper;
import com.smartwardrobe.model.dto.AdminDashboardDTO;
import com.smartwardrobe.model.dto.UserAdminDTO;
import com.smartwardrobe.model.entity.Clothes;
import com.smartwardrobe.model.entity.User;
import com.smartwardrobe.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClothesMapper clothesMapper;

    @Override
    public AdminDashboardDTO getDashboardStats() {
        AdminDashboardDTO stats = new AdminDashboardDTO();
        
        // Total Users
        Long totalUsers = userMapper.selectCount(new QueryWrapper<>());
        stats.setTotalUsers(totalUsers);
        
        // Active Users Today (Mocked logic or based on last login if available, using total users for now)
        stats.setActiveUsersToday(totalUsers > 0 ? totalUsers / 2 : 0);
        
        // Total Items
        Long totalItems = clothesMapper.selectCount(new QueryWrapper<>());
        stats.setTotalItems(totalItems);
        
        // Storage Used (calculate uploads folder size)
        stats.setStorageUsed(calculateUploadsDirectorySize());
        
        return stats;
    }

    @Override
    public Page<UserAdminDTO> getUserList(int page, int size, String keyword) {
        Page<User> userPage = new Page<>(page, size);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like("username", keyword).or().like("email", keyword);
        }
        queryWrapper.orderByDesc("create_time");
        
        Page<User> resultPage = userMapper.selectPage(userPage, queryWrapper);
        
        Page<UserAdminDTO> dtoPage = new Page<>(page, size, resultPage.getTotal());
        dtoPage.setRecords(resultPage.getRecords().stream().map(user -> {
            UserAdminDTO dto = new UserAdminDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setRegisterDate(user.getCreateTime());
            dto.setIsActive(user.getIsActive() != null ? user.getIsActive() : true);
            return dto;
        }).collect(Collectors.toList()));
        
        return dtoPage;
    }

    @Override
    public void toggleUserStatus(Long userId, Boolean isActive) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setIsActive(isActive);
            userMapper.updateById(user);
        }
    }

    private String calculateUploadsDirectorySize() {
        File folder = new File("uploads");
        long length = getFolderSize(folder);
        return formatSize(length);
    }

    private long getFolderSize(File folder) {
        long length = 0;
        if (folder.listFiles() != null) {
            for (File file : folder.listFiles()) {
                if (file.isFile()) {
                    length += file.length();
                } else {
                    length += getFolderSize(file);
                }
            }
        }
        return length;
    }

    private String formatSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
