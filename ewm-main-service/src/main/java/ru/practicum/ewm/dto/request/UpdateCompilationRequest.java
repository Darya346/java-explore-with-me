package ru.practicum.ewm.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationRequest {

    @Size(min = 1, max = 50)
    String title;

    Boolean pinned;
    Set<Long> events;
}