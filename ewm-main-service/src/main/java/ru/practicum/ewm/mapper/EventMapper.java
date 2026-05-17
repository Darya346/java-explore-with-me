package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.response.EventFullDto;
import ru.practicum.ewm.dto.response.EventShortDto;
import ru.practicum.ewm.dto.request.NewEventDto;
import ru.practicum.ewm.model.Event;

public class EventMapper {

    public static EventShortDto toShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(CategoryMapper.toDto(event.getCategory()));
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setInitiator(UserMapper.toDto(event.getInitiator()));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() : 0L);
        eventShortDto.setViews(event.getViews() != null ? event.getViews() : 0L);
        return eventShortDto;
    }

    public static EventFullDto toFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setCategory(CategoryMapper.toDto(event.getCategory()));
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setInitiator(UserMapper.toDto(event.getInitiator()));

        if (event.getLocation() != null) {
            eventFullDto.setLocation(LocationMapper.toDto(event.getLocation()));
        }

        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setState(event.getState());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() : 0L);
        eventFullDto.setViews(event.getViews() != null ? event.getViews() : 0L);
        return eventFullDto;
    }

    public static Event toEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setTitle(newEventDto.getTitle());
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.getRequestModeration());
        return event;
    }
}