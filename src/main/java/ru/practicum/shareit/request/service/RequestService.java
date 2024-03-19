package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.IncomingRequestDto;
import ru.practicum.shareit.request.dto.OutgoingRequestDto;

import java.util.List;

public interface RequestService {

    List<OutgoingRequestDto> getAllRequesterRequests(Long requesterId, Pageable pageable);

    List<OutgoingRequestDto> getAllRequests(Long userId, Pageable pageable);

    OutgoingRequestDto getRequestById(Long userId, Long requestId);

    OutgoingRequestDto postRequest(Long requesterId, IncomingRequestDto incomingRequestDto);

}
