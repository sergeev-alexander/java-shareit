package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.ValidationMarker;
import ru.practicum.shareit.request.dto.IncomingRequestDto;
import ru.practicum.shareit.request.dto.OutgoingRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/requests")
@Validated
@RequiredArgsConstructor
public class RequestController {

    private final String header = "X-Sharer-User-Id";
    private final Sort sortByCreatingDesc = Sort.by(Sort.Direction.DESC, "created");
    private final RequestService requestService;

    @GetMapping
    public Collection<OutgoingRequestDto> getAllRequesterRequests(
            HttpServletRequest request,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(value = "size", defaultValue = "20") @Min(1) @Max(100) Integer size,
            @RequestHeader(header) @Positive Long requesterId) {
        log.info("Id-{} {} {}?{}", requesterId, request.getMethod(), request.getRequestURI(), request.getQueryString());
        return requestService.getAllRequesterRequests(requesterId, PageRequest.of(offset / size, size, sortByCreatingDesc));
    }

    @GetMapping("/all")
    public Collection<OutgoingRequestDto> getAllRequests(
            HttpServletRequest request,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(value = "size", defaultValue = "20") @Min(1) @Max(100) Integer size,
            @RequestHeader(header) @Positive Long userId) {
        log.info("Id-{} {} {}?{}", userId, request.getMethod(), request.getRequestURI(), request.getQueryString());
        return requestService.getAllRequests(userId, PageRequest.of(offset / size, size, sortByCreatingDesc));
    }

    @GetMapping("/{requestId}")
    public OutgoingRequestDto getRequestById(
            HttpServletRequest request,
            @RequestHeader(header) @Positive Long userId,
            @PathVariable @Positive Long requestId) {
        log.info("Id-{} {} {}", userId, request.getMethod(), request.getRequestURI());
        return requestService.getRequestById(userId, requestId);
    }

    @PostMapping
    public OutgoingRequestDto postRequest(
            HttpServletRequest request,
            @RequestHeader(header) @Positive Long requesterId,
            @RequestBody @Validated(ValidationMarker.OnCreate.class) IncomingRequestDto incomingRequestDto) {
        log.info("Id-{} {} {} {}", requesterId, request.getMethod(), request.getRequestURI(), incomingRequestDto);
        return requestService.postRequest(requesterId, incomingRequestDto);
    }

}
