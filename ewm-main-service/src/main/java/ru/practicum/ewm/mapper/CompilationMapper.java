package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.response.CompilationDto;
import ru.practicum.ewm.dto.response.EventShortDto;
import ru.practicum.ewm.dto.request.NewCompilationDto;
import ru.practicum.ewm.model.Compilation;

import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static CompilationDto toDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.getPinned());

        if (compilation.getEvents() != null) {
            Set<EventShortDto> eventDtos = compilation.getEvents().stream()
                    .map(EventMapper::toShortDto)
                    .collect(Collectors.toSet());
            compilationDto.setEvents(eventDtos);
        }

        return compilationDto;
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.getPinned());
        return compilation;
    }
}