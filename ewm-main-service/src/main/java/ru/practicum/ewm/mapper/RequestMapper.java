package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.response.RequestDto;
import ru.practicum.ewm.model.ParticipationRequest;

public class RequestMapper {

    public static RequestDto toDto(ParticipationRequest request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setStatus(request.getStatus());
        requestDto.setCreated(request.getCreated());
        return requestDto;
    }
}