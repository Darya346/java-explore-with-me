package ru.practicum.ewm.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.dto.response.LocationDto;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdmin {

    @Size(min = 3, max = 120)
    String title;

    @Size(min = 20, max = 2000)
    String annotation;

    @Size(min = 20, max = 7000)
    String description;

    Long category;
    LocationDto location;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    String stateAction;
}