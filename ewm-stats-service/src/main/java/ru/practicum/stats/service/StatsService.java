package ru.practicum.stats.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void hit(EndpointHitDto dto);

    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end,
                                List<String> uris, boolean unique);
}