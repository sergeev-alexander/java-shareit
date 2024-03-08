package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestMapperTest {

    @Test
    void mapIncomingDtoToRequest_normalBehavior() {
        IncomingRequestDto incomingRequestDto = new IncomingRequestDto(
                null,
                "Some incomingRequestDto description");
        Request request = new Request(
                null,
                "Some incomingRequestDto description",
                null,
                null);
        assertEquals(RequestMapper.mapIncomingDtoToRequest(incomingRequestDto).getDescription(),
                request.getDescription());
    }

    @Test
    void mapRequestToOutgoingDto_normalBehavior() {
        Request request = new Request(
                1L,
                "Some request description",
                LocalDateTime.of(2000, 1, 1, 1, 1, 1),
                new User(
                        2L,
                        null,
                        null));
        OutgoingRequestDto outgoingRequestDto = new OutgoingRequestDto(
                1L,
                "Some request description",
                LocalDateTime.of(2000, 1, 1, 1, 1, 1),
                2L,
                List.of());
        OutgoingRequestDto result = RequestMapper.mapRequestToOutgoingDto(request);
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getCreated(), result.getCreated());
        assertEquals(request.getRequester().getId(), result.getRequesterId());
    }

}
