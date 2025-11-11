package com.smartwardrobe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartwardrobe.mapper.ClothesMapper;
import com.smartwardrobe.model.dto.ClothingDTO;
import com.smartwardrobe.model.entity.Clothes;
import com.smartwardrobe.service.ClothesService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClothesServiceImpl implements ClothesService {

    private final ClothesMapper clothesMapper;

    public ClothesServiceImpl(ClothesMapper clothesMapper) {
        this.clothesMapper = clothesMapper;
    }

    @Override
    public IPage<Clothes> listClothes(Long userId, Page<Clothes> page) {
        return clothesMapper.selectPage(page, new LambdaQueryWrapper<Clothes>()
                .eq(Clothes::getUserId, userId)
                .orderByDesc(Clothes::getCreateTime));
    }

    @Override
    public Clothes createClothes(Long userId, ClothingDTO dto) {
        Clothes clothes = new Clothes();
        clothes.setUserId(userId);
        clothes.setName(dto.getName());
        clothes.setCategory(dto.getCategory());
        clothes.setColor(dto.getColor());
        clothes.setSeason(dto.getSeason());
        clothes.setTags(dto.getTags());
        clothes.setBrand(dto.getBrand());
        clothes.setPrice(dto.getPrice());
        clothes.setPurchaseDate(dto.getPurchaseDate());
        clothes.setImageUrl(dto.getImageUrl());
        clothes.setDescription(dto.getDescription());
        clothes.setCreateTime(LocalDateTime.now());
        clothesMapper.insert(clothes);
        return clothes;
    }

    @Override
    public Clothes updateClothes(Long userId, Long id, ClothingDTO dto) {
        Clothes clothes = clothesMapper.selectOne(new LambdaQueryWrapper<Clothes>()
                .eq(Clothes::getId, id)
                .eq(Clothes::getUserId, userId));
        if (clothes == null) {
            throw new IllegalArgumentException("Clothing item not found");
        }
        clothes.setName(dto.getName());
        clothes.setCategory(dto.getCategory());
        clothes.setColor(dto.getColor());
        clothes.setSeason(dto.getSeason());
        clothes.setTags(dto.getTags());
        clothes.setBrand(dto.getBrand());
        clothes.setPrice(dto.getPrice());
        clothes.setPurchaseDate(dto.getPurchaseDate());
        clothes.setImageUrl(dto.getImageUrl());
        clothes.setDescription(dto.getDescription());
        clothesMapper.updateById(clothes);
        return clothes;
    }

    @Override
    public void deleteClothes(Long userId, Long id) {
        int deleted = clothesMapper.delete(new LambdaQueryWrapper<Clothes>()
                .eq(Clothes::getId, id)
                .eq(Clothes::getUserId, userId));
        if (deleted == 0) {
            throw new IllegalArgumentException("Clothing item not found");
        }
    }
}
