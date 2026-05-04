package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.response.LocationDto;
import ru.practicum.ewm.dto.request.NewLocationDto;
import ru.practicum.ewm.model.Location;

public class LocationMapper {

    public static LocationDto toDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(location.getId());
        locationDto.setName(location.getName());
        locationDto.setLat(location.getLat());
        locationDto.setLon(location.getLon());
        locationDto.setRadius(location.getRadius());
        return locationDto;
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