package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.response.LocationDto;
import ru.practicum.ewm.service.LocationService;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class PublicLocationController {

    private final LocationService locationService;

    @GetMapping
    public List<LocationDto> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{locationId}")
    public LocationDto getLocation(@PathVariable Long locationId) {
        return locationService.getLocation(locationId);
    }
}