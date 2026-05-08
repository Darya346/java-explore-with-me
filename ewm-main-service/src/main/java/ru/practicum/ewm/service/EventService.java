package ru.practicum.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.ewm.dto.request.NewEventDto;
import ru.practicum.ewm.dto.request.UpdateEventAdmin;
import ru.practicum.ewm.dto.request.UpdateEventUser;
import ru.practicum.ewm.dto.response.EventFullDto;
import ru.practicum.ewm.dto.response.EventShortDto;
import ru.practicum.ewm.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    // Private
    List<EventShortDto> getEventsByUserId(Long userId, int from, int size);
    EventFullDto createEvent(Long userId, NewEventDto newEventDto);
    EventFullDto getEventByIdAndUserId(Long userId, Long eventId);
    EventFullDto updateEventByUserId(Long userId, Long eventId, UpdateEventUser updateRequest);

    // Admin
    List<EventFullDto> getEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);
    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdmin updateRequest);

    // Public
    List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                        Boolean onlyAvailable, String sort, int from, int size,
                                        HttpServletRequest request);
    EventFullDto getEventByIdPublic(Long id, HttpServletRequest request);
}