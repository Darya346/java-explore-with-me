package ru.practicum.ewm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewLocationDto {

    @NotBlank
    String name;

    @NotNull
    Float lat;

    @NotNull
    Float lon;

    @NotNull
    Float radius;
}