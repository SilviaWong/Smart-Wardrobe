package com.smartwardrobe.model.vo;

import java.util.Map;

public class StatsResponse {

    private long total;
    private Map<String, Long> byCategory;
    private Map<String, Long> byColor;
    private Map<String, Long> bySeason;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Map<String, Long> getByCategory() {
        return byCategory;
    }

    public void setByCategory(Map<String, Long> byCategory) {
        this.byCategory = byCategory;
    }

    public Map<String, Long> getByColor() {
        return byColor;
    }

    public void setByColor(Map<String, Long> byColor) {
        this.byColor = byColor;
    }

    public Map<String, Long> getBySeason() {
        return bySeason;
    }

    public void setBySeason(Map<String, Long> bySeason) {
        this.bySeason = bySeason;
    }
}
