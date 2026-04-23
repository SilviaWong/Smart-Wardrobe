package com.smartwardrobe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.smartwardrobe.mapper.ClothesMapper;
import com.smartwardrobe.mapper.WearLogMapper;
import com.smartwardrobe.model.entity.Clothes;
import com.smartwardrobe.model.entity.WearLog;
import com.smartwardrobe.service.WearLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WearLogServiceImpl implements WearLogService {

    private final WearLogMapper wearLogMapper;
    private final ClothesMapper clothesMapper;

    public WearLogServiceImpl(WearLogMapper wearLogMapper, ClothesMapper clothesMapper) {
        this.wearLogMapper = wearLogMapper;
        this.clothesMapper = clothesMapper;
    }

    @Override
    @Transactional
    public void logWear(Long userId, LocalDate date, Long itemId, Long outfitId) {
        WearLog log = new WearLog();
        log.setUserId(userId);
        log.setWearDate(date);
        log.setItemId(itemId);
        log.setOutfitId(outfitId);
        log.setCreateTime(LocalDateTime.now());
        wearLogMapper.insert(log);

        if (itemId != null) {
            updateClothesStats(itemId);
        }
    }

    private void updateClothesStats(Long itemId) {
        Clothes clothes = clothesMapper.selectById(itemId);
        if (clothes != null) {
            clothesMapper.update(null, new LambdaUpdateWrapper<Clothes>()
                    .eq(Clothes::getId, itemId)
                    .set(Clothes::getWearCount, (clothes.getWearCount() == null ? 0 : clothes.getWearCount()) + 1)
                    .set(Clothes::getLastWornDate, LocalDateTime.now()));
        }
    }

    @Override
    public List<WearLog> listLogs(Long userId, LocalDate from, LocalDate to) {
        return wearLogMapper.selectList(new LambdaQueryWrapper<WearLog>()
                .eq(WearLog::getUserId, userId)
                .ge(from != null, WearLog::getWearDate, from)
                .le(to != null, WearLog::getWearDate, to)
                .orderByDesc(WearLog::getWearDate));
    }
}
