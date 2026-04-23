package com.smartwardrobe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartwardrobe.model.dto.OutfitDTO;
import com.smartwardrobe.model.entity.Outfit;

public interface OutfitService {

    IPage<Outfit> listOutfits(Long userId, Page<Outfit> page);

    Outfit createOutfit(Long userId, OutfitDTO dto);

    void deleteOutfit(Long userId, Long id);
}
