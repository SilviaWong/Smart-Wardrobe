package com.smartwardrobe.service;

import com.smartwardrobe.model.vo.StatsResponse;

public interface StatsService {

    StatsResponse summarize(Long userId);
}
