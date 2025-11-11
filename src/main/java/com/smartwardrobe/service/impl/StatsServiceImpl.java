package com.smartwardrobe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartwardrobe.mapper.ClothesMapper;
import com.smartwardrobe.model.entity.Clothes;
import com.smartwardrobe.model.vo.StatsResponse;
import com.smartwardrobe.service.StatsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StatsServiceImpl implements StatsService {

    private final ClothesMapper clothesMapper;

    public StatsServiceImpl(ClothesMapper clothesMapper) {
        this.clothesMapper = clothesMapper;
    }

    @Override
    public StatsResponse summarize(Long userId) {
        List<Clothes> clothesList = clothesMapper.selectList(new LambdaQueryWrapper<Clothes>()
                .eq(Clothes::getUserId, userId));

        StatsResponse response = new StatsResponse();
        response.setTotal(clothesList.size());
        response.setByCategory(groupByAttribute(clothesList, Clothes::getCategory));
        response.setByColor(groupByAttribute(clothesList, Clothes::getColor));
        response.setBySeason(groupByAttribute(clothesList, Clothes::getSeason));
        return response;
    }

    private Map<String, Long> groupByAttribute(List<Clothes> clothesList, Function<Clothes, String> extractor) {
        return clothesList.stream()
                .map(extractor)
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
