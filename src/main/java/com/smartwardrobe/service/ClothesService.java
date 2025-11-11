package com.smartwardrobe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartwardrobe.model.dto.ClothingDTO;
import com.smartwardrobe.model.entity.Clothes;

public interface ClothesService {

    IPage<Clothes> listClothes(Long userId, Page<Clothes> page);

    Clothes createClothes(Long userId, ClothingDTO dto);

    Clothes updateClothes(Long userId, Long id, ClothingDTO dto);

    void deleteClothes(Long userId, Long id);
}
