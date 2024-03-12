package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.dto.OutgoingBookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exeption.ValidationMarker;
import ru.practicum.shareit.validation.BookingStateValidation;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.Collection;

import static ru.practicum.shareit.http.HttpHeader.header;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
    private final BookingService bookingService;

    @GetMapping
    public Collection<OutgoingBookingDto> getAllUserBookings(
            HttpServletRequest request,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer firstElement,
            @RequestParam(value = "size", defaultValue = "20") @Min(1) @Max(20) Integer size,
            @RequestHeader(header) @Positive Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") @BookingStateValidation String bookingStateString) {
        log.info("Id-{} {} {}?{}", userId, request.getMethod(), request.getRequestURI(), request.getQueryString());
        return bookingService.getAllUserBookings(userId, BookingState.valueOf(bookingStateString),
                PageRequest.of(firstElement / size, size, sortByStartDesc));
    }

    @GetMapping("/owner")
    public Collection<OutgoingBookingDto> getAllOwnerItemBookings(
            HttpServletRequest request,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer firstElement,
            @RequestParam(value = "size", defaultValue = "20") @Min(1) @Max(20) Integer size,
            @RequestHeader(header) @Positive Long ownerId,
            @RequestParam(name = "state", defaultValue = "ALL") @BookingStateValidation String bookingStateString) {
        log.info("Id-{} {} {}?{}", ownerId, request.getMethod(), request.getRequestURI(), request.getQueryString());
        return bookingService.getAllOwnerItemBookings(ownerId, BookingState.valueOf(bookingStateString),
                PageRequest.of(firstElement / size, size, sortByStartDesc));
    }

    @GetMapping("/{bookingId}")
    public OutgoingBookingDto getBookingById(HttpServletRequest request,
                                             @RequestHeader(header) @Positive Long userId,
                                             @PathVariable @Positive Long bookingId) {
        log.info("Id-{} {} {}", userId, request.getMethod(), request.getRequestURI());
        return bookingService.getBookingById(userId, bookingId);
    }

    @PostMapping
    public OutgoingBookingDto postBooking(
            HttpServletRequest request,
            @RequestHeader(header) @Positive Long bookerId,
            @RequestBody @Validated(ValidationMarker.OnCreate.class) IncomingBookingDto incomingBookingDto) {
        log.info("Id-{} {} {} {}", bookerId, request.getMethod(), request.getRequestURI(), incomingBookingDto);
        return bookingService.postBooking(bookerId, incomingBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public OutgoingBookingDto patchBooking(HttpServletRequest request,
                                           @RequestHeader(header) @Positive Long itemOwnerId,
                                           @PathVariable @Positive Long bookingId,
                                           @RequestParam Boolean approved) {
        log.info("Id-{} {} {}", itemOwnerId, request.getMethod(), request.getRequestURI());
        return bookingService.patchBookingById(itemOwnerId, bookingId, approved);
    }

}
