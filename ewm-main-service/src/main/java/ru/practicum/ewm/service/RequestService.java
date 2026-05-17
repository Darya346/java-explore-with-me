package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.request.RequestStatusUpdate;
import ru.practicum.ewm.dto.response.RequestDto;
import ru.practicum.ewm.dto.response.RequestStatusUpdateResult;

import java.util.List;

public interface RequestService {
    // Для тех, кто подает заявку (Requester)
    List<RequestDto> getRequestsByUserId(Long userId);
    RequestDto createRequest(Long userId, Long eventId);
    RequestDto cancelRequest(Long userId, Long requestId);

    // Для организатора события (Initiator)
    List<RequestDto> getEventRequests(Long userId, Long eventId);
    RequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, RequestStatusUpdate updateRequest);
}