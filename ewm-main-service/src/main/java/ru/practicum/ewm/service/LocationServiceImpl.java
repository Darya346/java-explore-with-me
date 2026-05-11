package ru.practicum.ewm.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.request.NewLocationDto;
import ru.practicum.ewm.dto.response.LocationDto;
import ru.practicum.ewm.mapper.LocationMapper;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.repository.LocationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class LocationServiceImpl implements LocationService {

    LocationRepository locationRepository;

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
                .orElseThrow(() -> new RuntimeException("Location not found"));
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
    }
}