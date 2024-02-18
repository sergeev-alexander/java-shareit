package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.dto.OutgoingBookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exeption.ValidationMarker;
import ru.practicum.shareit.validation.BookingStateValidation;

import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final String header = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @GetMapping
    public Collection<OutgoingBookingDto> getAllUserBookings(
            @RequestHeader(header) @Positive Long userId,
            @RequestParam(name = "state", defaultValue = "ALL")
            @BookingStateValidation String bookingStateString) {
        return bookingService.getAllUserBookings(userId, BookingState.valueOf(bookingStateString));
    }

    @GetMapping("/owner")
    public Collection<OutgoingBookingDto> getAllOwnerItemsBookings(
            @RequestHeader(header) @Positive Long ownerId,
            @RequestParam(name = "state", defaultValue = "ALL")
            @BookingStateValidation String bookingStateString) {
        return bookingService.getAllOwnerItemBookings(ownerId, BookingState.valueOf(bookingStateString));
    }

    @GetMapping("/{bookingId}")
    public OutgoingBookingDto getBookingById(@RequestHeader(header) @Positive Long userId,
                                             @PathVariable @Positive Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @PostMapping
    public OutgoingBookingDto postBooking(
            @RequestHeader(header) @Positive Long bookerId,
            @RequestBody @Validated(ValidationMarker.OnCreate.class) IncomingBookingDto incomingBookingDto) {
        return bookingService.postBooking(bookerId, incomingBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public OutgoingBookingDto patchBooking(@RequestHeader(header) @Positive Long itemOwnerId,
                                           @PathVariable @Positive Long bookingId,
                                           @RequestParam Boolean approved) {
        return bookingService.patchBookingById(itemOwnerId, bookingId, approved);
    }

}
