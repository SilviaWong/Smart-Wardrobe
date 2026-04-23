package com.smartwardrobe.service;

import com.smartwardrobe.model.entity.Location;
import java.util.List;

public interface LocationService {

    List<Location> listLocations(Long userId);

    Location createLocation(Long userId, String name, Long parentId);

    void deleteLocation(Long userId, Long id);
}
