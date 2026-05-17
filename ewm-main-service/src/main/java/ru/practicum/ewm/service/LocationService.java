package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.request.NewLocationDto;
import ru.practicum.ewm.dto.response.EventShortDto;
import ru.practicum.ewm.dto.response.LocationDto;

import java.util.List;

public interface LocationService {

    LocationDto createLocation(NewLocationDto newLocationDto);

    LocationDto getLocation(Long locationId);

    List<LocationDto> getAllLocations();

    void deleteLocation(Long locationId);

    List<EventShortDto> getEventsInLocation(Long locationId);

    LocationDto updateLocation(Long locationId, NewLocationDto dto);
}