package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class RequestMapper {

    public Request mapIncomingDtoToRequest(IncomingRequestDto incomingRequestDto) {
        return new Request(
                null,
                incomingRequestDto.getDescription(),
                LocalDateTime.now(),
                null);
    }

    public OutgoingRequestDto mapRequestToOutgoingDto(Request request) {
        return new OutgoingRequestDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                request.getRequester().getId(),
                List.of());
    }
}
