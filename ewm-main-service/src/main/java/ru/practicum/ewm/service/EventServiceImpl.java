package ru.practicum.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatsClient;
import ru.practicum.ewm.dto.request.NewEventDto;
import ru.practicum.ewm.dto.request.UpdateEventAdmin;
import ru.practicum.ewm.dto.request.UpdateEventUser;
import ru.practicum.ewm.dto.response.EventFullDto;
import ru.practicum.ewm.dto.response.EventShortDto;
import ru.practicum.ewm.dto.response.LocationDto; // Проверь этот импорт
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.mapper.LocationMapper;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    EventRepository eventRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;
    LocationRepository locationRepository;
    StatsClient statsClient;

    static String APP_NAME = "ewm-main-service";

    // --- PRIVATE API ---

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto dto) {

        if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Event date must be at least 2 hours in the future.");
        }

        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category with id=" + dto.getCategory() + " was not found"));


        Location location = locationRepository.save(LocationMapper.toLocation(dto.getLocation()));

        Event event = EventMapper.toEvent(dto);
        event.setInitiator(initiator);
        event.setCategory(category);
        event.setLocation(location);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0L);
        event.setViews(0L);


        if (event.getPaid() == null) event.setPaid(false);
        if (event.getParticipantLimit() == null) event.setParticipantLimit(0);
        if (event.getRequestModeration() == null) event.setRequestModeration(true);

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getEventsByUserId(Long userId, int from, int size) {
        return eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size))
                .stream().map(EventMapper::toShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByIdAndUserId(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found"));
        return EventMapper.toFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByUserId(Long userId, Long eventId, UpdateEventUser request) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }

        if (request.getEventDate() != null) {

            if (request.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("Event date must be at least 2 hours in the future.");
            }
            event.setEventDate(request.getEventDate());
        }

        if (request.getStateAction() != null) {
            if (request.getStateAction().equals("SEND_TO_REVIEW")) {
                event.setState(EventState.PENDING);
            } else if (request.getStateAction().equals("CANCEL_REVIEW")) {
                event.setState(EventState.CANCELED);
            }
        }


        updateEventCommonFields(event, request.getAnnotation(), request.getCategory(), request.getDescription(),
                request.getLocation(), request.getPaid(), request.getParticipantLimit(), request.getRequestModeration(), request.getTitle());

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    // --- ADMIN API ---

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                               LocalDateTime start, LocalDateTime end, int from, int size) {
        return eventRepository.findEventsByAdmin(users, states, categories, start, end, PageRequest.of(from / size, size))
                .stream().map(EventMapper::toFullDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdmin request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (request.getEventDate() != null) {
            if (request.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new BadRequestException("Event date must be at least 1 hour before publication.");
            }
            event.setEventDate(request.getEventDate());
        }

        if (request.getStateAction() != null) {
            if (request.getStateAction().equals("PUBLISH_EVENT")) {
                if (event.getState() != EventState.PENDING) {
                    throw new ConflictException("Cannot publish the event because it's not in the PENDING state.");
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (request.getStateAction().equals("REJECT_EVENT")) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new ConflictException("Cannot reject the event because it's already published.");
                }
                event.setState(EventState.CANCELED);
            }
        }

        updateEventCommonFields(event, request.getAnnotation(), request.getCategory(), request.getDescription(),
                request.getLocation(), request.getPaid(), request.getParticipantLimit(), request.getRequestModeration(), request.getTitle());

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    // --- PUBLIC API ---

    @Override
    public List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Boolean onlyAvailable, String sort, int from, int size,
                                               HttpServletRequest request) {

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Start date must be before end date");
        }

        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }

        sendStats(request);

        int page = from / size;
        PageRequest pageable = PageRequest.of(page, size);

        List<Event> events = eventRepository.findPublishedEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, pageable);

        if ("VIEWS".equals(sort)) {
            events = events.stream()
                    .sorted((e1, e2) -> {
                        long v1 = (e1.getViews() == null) ? 0 : e1.getViews();
                        long v2 = (e2.getViews() == null) ? 0 : e2.getViews();
                        return Long.compare(v2, v1);
                    })
                    .collect(Collectors.toList());
        }

        return events.stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }
    @Override
    public EventFullDto getEventByIdPublic(Long id, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Published event not found"));

        sendStats(request);
        event.setViews(event.getViews() + 1);
        eventRepository.save(event);

        return EventMapper.toFullDto(event);
    }



    private void updateEventCommonFields(Event event, String annotation, Long catId, String description,
                                         LocationDto locationDto, Boolean paid, Integer partLimit, Boolean reqMod, String title) {
        if (annotation != null && !annotation.isBlank()) event.setAnnotation(annotation);
        if (catId != null) {
            event.setCategory(categoryRepository.findById(catId)
                    .orElseThrow(() -> new NotFoundException("Category not found")));
        }
        if (description != null && !description.isBlank()) event.setDescription(description);


        if (locationDto != null) {
            Location location = locationRepository.save(LocationMapper.toLocation(locationDto));
            event.setLocation(location);
        }

        if (paid != null) event.setPaid(paid);
        if (partLimit != null) {
            if (partLimit < 0) {
                throw new BadRequestException("Participant limit cannot be negative");
            }
            event.setParticipantLimit(partLimit);
        }
        if (reqMod != null) event.setRequestModeration(reqMod);
        if (title != null && !title.isBlank()) event.setTitle(title);
    }


    private void sendStats(HttpServletRequest request) {
        try {
            statsClient.hit(APP_NAME, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        } catch (Exception e) {
            log.error("Ошибка статистики: {}", e.getMessage());
        }
    }
}