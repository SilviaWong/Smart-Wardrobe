package com.smartwardrobe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartwardrobe.model.entity.Outfit;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OutfitMapper extends BaseMapper<Outfit> {
}
