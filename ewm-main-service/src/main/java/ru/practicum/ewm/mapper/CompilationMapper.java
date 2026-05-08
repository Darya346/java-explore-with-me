package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.request.NewCompilationDto;
import ru.practicum.ewm.dto.response.CompilationDto;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto dto, Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setTitle(dto.getTitle());
        compilation.setEvents(events);
        compilation.setPinned(dto.getPinned() != null ? dto.getPinned() : false);
        return compilation;
    }

    public static CompilationDto toDto(Compilation compilation) {
        CompilationDto dto = new CompilationDto();
        dto.setId(compilation.getId());
        dto.setTitle(compilation.getTitle());
        dto.setPinned(compilation.getPinned());

        if (compilation.getEvents() != null) {

            dto.setEvents(compilation.getEvents().stream()
                    .map(EventMapper::toShortDto)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }
}