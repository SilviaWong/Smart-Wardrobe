package com.smartwardrobe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartwardrobe.model.entity.Clothes;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClothesMapper extends BaseMapper<Clothes> {
}
