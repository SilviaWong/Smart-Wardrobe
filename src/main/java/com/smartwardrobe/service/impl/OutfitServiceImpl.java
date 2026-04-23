package com.smartwardrobe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartwardrobe.mapper.OutfitItemMapper;
import com.smartwardrobe.mapper.OutfitMapper;
import com.smartwardrobe.model.dto.OutfitDTO;
import com.smartwardrobe.model.entity.Outfit;
import com.smartwardrobe.model.entity.OutfitItem;
import com.smartwardrobe.service.OutfitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class OutfitServiceImpl implements OutfitService {

    private final OutfitMapper outfitMapper;
    private final OutfitItemMapper outfitItemMapper;

    public OutfitServiceImpl(OutfitMapper outfitMapper, OutfitItemMapper outfitItemMapper) {
        this.outfitMapper = outfitMapper;
        this.outfitItemMapper = outfitItemMapper;
    }

    @Override
    public IPage<Outfit> listOutfits(Long userId, Page<Outfit> page) {
        return outfitMapper.selectPage(page, new LambdaQueryWrapper<Outfit>()
                .eq(Outfit::getUserId, userId)
                .orderByDesc(Outfit::getCreateTime));
    }

    @Override
    @Transactional
    public Outfit createOutfit(Long userId, OutfitDTO dto) {
        Outfit outfit = new Outfit();
        outfit.setUserId(userId);
        outfit.setName(dto.getName());
        outfit.setCompositeImageUrl(dto.getCompositeImageUrl());
        outfit.setSeason(dto.getSeason());
        outfit.setCreateTime(LocalDateTime.now());
        outfitMapper.insert(outfit);

        if (dto.getItems() != null) {
            for (OutfitDTO.ItemMappingDTO itemDto : dto.getItems()) {
                OutfitItem item = new OutfitItem();
                item.setOutfitId(outfit.getId());
                item.setItemId(itemDto.getItemId());
                item.setzIndex(itemDto.getzIndex());
                item.setScale(itemDto.getScale());
                item.setRotation(itemDto.getRotation());
                item.setOffsetX(itemDto.getOffsetX());
                item.setOffsetY(itemDto.getOffsetY());
                outfitItemMapper.insert(item);
            }
        }

        return outfit;
    }

    @Override
    @Transactional
    public void deleteOutfit(Long userId, Long id) {
        // Verify ownership
        Outfit outfit = outfitMapper.selectOne(new LambdaQueryWrapper<Outfit>()
                .eq(Outfit::getUserId, userId)
                .eq(Outfit::getId, id));
        if (outfit != null) {
            outfitItemMapper.delete(new LambdaQueryWrapper<OutfitItem>()
                    .eq(OutfitItem::getOutfitId, id));
            outfitMapper.deleteById(id);
        }
    }
}
