package com.smartwardrobe.service;

import com.smartwardrobe.model.entity.WearLog;
import java.time.LocalDate;
import java.util.List;

public interface WearLogService {
    void logWear(Long userId, LocalDate date, Long itemId, Long outfitId);
    List<WearLog> listLogs(Long userId, LocalDate from, LocalDate to);
}
