package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.response.LocationDto;
import ru.practicum.ewm.dto.request.NewLocationDto;
import ru.practicum.ewm.model.Location;

public class LocationMapper {

    public static LocationDto toDto(Location location) {
        LocationDto dto = new LocationDto();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setLat(location.getLat());
        dto.setLon(location.getLon());
        dto.setRadius(location.getRadius());
        return dto;
    }

    public static Location toLocation(NewLocationDto request) {
        Location location = new Location();
        location.setName(request.getName());
        location.setLat(request.getLat());
        location.setLon(request.getLon());
        location.setRadius(request.getRadius());
        return location;
    }
}