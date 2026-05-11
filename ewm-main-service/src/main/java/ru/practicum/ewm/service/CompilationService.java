package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.request.NewCompilationDto;
import ru.practicum.ewm.dto.request.UpdateCompilationRequest;
import ru.practicum.ewm.dto.response.CompilationDto;

import java.util.List;

public interface CompilationService {
    // Admin API
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);
    void deleteCompilation(Long compId);
    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateRequest);

    // Public API
    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);
    CompilationDto getCompilationById(Long compId);
}