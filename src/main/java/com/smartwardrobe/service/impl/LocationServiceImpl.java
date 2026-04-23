package com.smartwardrobe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartwardrobe.mapper.LocationMapper;
import com.smartwardrobe.model.entity.Location;
import com.smartwardrobe.service.LocationService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationMapper locationMapper;

    public LocationServiceImpl(LocationMapper locationMapper) {
        this.locationMapper = locationMapper;
    }

    @Override
    public List<Location> listLocations(Long userId) {
        return locationMapper.selectList(new LambdaQueryWrapper<Location>()
                .eq(Location::getUserId, userId)
                .orderByAsc(Location::getId));
    }

    @Override
    public Location createLocation(Long userId, String name, Long parentId) {
        Location location = new Location();
        location.setUserId(userId);
        location.setName(name);
        location.setParentId(parentId);
        location.setCreateTime(LocalDateTime.now());
        locationMapper.insert(location);
        return location;
    }

    @Override
    public void deleteLocation(Long userId, Long id) {
        locationMapper.delete(new LambdaQueryWrapper<Location>()
                .eq(Location::getUserId, userId)
                .eq(Location::getId, id));
    }
}
