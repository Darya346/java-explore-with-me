package ru.practicum.ewm.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationDto {
    Long id;
    String name;
    Float lat;
    Float lon;
    Float radius;
}