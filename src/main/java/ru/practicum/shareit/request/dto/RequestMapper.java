package ru.practicum.shareit.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RequestMapper {

    public static Request mapIncomingDtoToRequest(IncomingRequestDto incomingRequestDto) {
        return new Request(
                null,
                incomingRequestDto.getDescription(),
                LocalDateTime.now(),
                null);
    }

    public static OutgoingRequestDto mapRequestToOutgoingDto(Request request) {
        return new OutgoingRequestDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                request.getRequester().getId(),
                List.of());
    }
}
