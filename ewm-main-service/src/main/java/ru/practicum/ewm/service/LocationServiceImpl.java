package ru.practicum.ewm.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.request.NewLocationDto;
import ru.practicum.ewm.dto.response.EventShortDto;
import ru.practicum.ewm.dto.response.LocationDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.mapper.LocationMapper;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.LocationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class LocationServiceImpl implements LocationService {

    LocationRepository locationRepository;
    private final EventRepository eventRepository;
    @Override
    @Transactional
    public LocationDto createLocation(NewLocationDto newLocationDto) {
        Location location = LocationMapper.toLocation(newLocationDto);
        Location savedLocation = locationRepository.save(location);
        return LocationMapper.toDto(savedLocation);
    }
    @Override
    public LocationDto getLocation(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundException("Location with id=" + locationId + " was not found"));
        return LocationMapper.toDto(location);
    }

    @Override
    public List<LocationDto> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(LocationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteLocation(Long locationId) {
        locationRepository.deleteById(locationId);
        if (!locationRepository.existsById(locationId)) {
            throw new NotFoundException("Location with id=" + locationId + " was not found");
        }
    }

    @Override
    public List<EventShortDto> getEventsInLocation(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundException("Location with id=" + locationId + " was not found")); // Добавлен ID

        return eventRepository.findEventsNearby(location.getLat(), location.getLon(), location.getRadius())
                .stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LocationDto updateLocation(Long locationId, NewLocationDto dto) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundException("Location with id=" + locationId + " was not found")); // Добавлен ID

        if (dto.getName() != null && !dto.getName().isBlank()) location.setName(dto.getName());
        if (dto.getLat() != null) location.setLat(dto.getLat());
        if (dto.getLon() != null) location.setLon(dto.getLon());
        if (dto.getRadius() != null) location.setRadius(dto.getRadius());

        return LocationMapper.toDto(locationRepository.save(location));
    }
}