package com.smartwardrobe.model.dto;

import java.util.List;

public class OutfitDTO {
    private String name;
    private String compositeImageUrl;
    private String season;
    private List<ItemMappingDTO> items;

    public static class ItemMappingDTO {
        private Long itemId;
        private Integer zIndex;
        private Float scale;
        private Float rotation;
        private Float offsetX;
        private Float offsetY;

        public Long getItemId() { return itemId; }
        public void setItemId(Long itemId) { this.itemId = itemId; }
        public Integer getzIndex() { return zIndex; }
        public void setzIndex(Integer zIndex) { this.zIndex = zIndex; }
        public Float getScale() { return scale; }
        public void setScale(Float scale) { this.scale = scale; }
        public Float getRotation() { return rotation; }
        public void setRotation(Float rotation) { this.rotation = rotation; }
        public Float getOffsetX() { return offsetX; }
        public void setOffsetX(Float offsetX) { this.offsetX = offsetX; }
        public Float getOffsetY() { return offsetY; }
        public void setOffsetY(Float offsetY) { this.offsetY = offsetY; }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCompositeImageUrl() { return compositeImageUrl; }
    public void setCompositeImageUrl(String compositeImageUrl) { this.compositeImageUrl = compositeImageUrl; }
    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }
    public List<ItemMappingDTO> getItems() { return items; }
    public void setItems(List<ItemMappingDTO> items) { this.items = items; }
}
