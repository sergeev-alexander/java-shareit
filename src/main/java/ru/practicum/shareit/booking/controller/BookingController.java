package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        log.info("\nGET /bookings/?state={}\n{} {}\n", bookingStateString, header, userId);
        return bookingService.getAllUserBookings(userId, BookingState.valueOf(bookingStateString));
    }

    @GetMapping("/owner")
    public Collection<OutgoingBookingDto> getAllOwnerItemsBookings(
            @RequestHeader(header) @Positive Long ownerId,
            @RequestParam(name = "state", defaultValue = "ALL")
            @BookingStateValidation String bookingStateString) {
        log.info("\nGET /bookings/?state={}/owner\n{} {}\n", bookingStateString, header, ownerId);
        return bookingService.getAllOwnerItemBookings(ownerId, BookingState.valueOf(bookingStateString));
    }

    @GetMapping("/{bookingId}")
    public OutgoingBookingDto getBookingById(@RequestHeader(header) @Positive Long userId,
                                             @PathVariable @Positive Long bookingId) {
        log.info("\nGET /bookings/{}\n{} {}\n", bookingId, header, userId);
        return bookingService.getBookingById(userId, bookingId);
    }

    @PostMapping
    public OutgoingBookingDto postBooking(
            @RequestHeader(header) @Positive Long bookerId,
            @RequestBody @Validated(ValidationMarker.OnCreate.class) IncomingBookingDto incomingBookingDto) {
        log.info("\nPOST /bookings\n{} {}\n{}\n", header, bookerId, incomingBookingDto);
        return bookingService.postBooking(bookerId, incomingBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public OutgoingBookingDto patchBooking(@RequestHeader(header) @Positive Long itemOwnerId,
                                           @PathVariable @Positive Long bookingId,
                                           @RequestParam Boolean approved) {
        log.info("\nPATCH /bookings/{}/approved={}\n{} {}\n", bookingId, approved, header, itemOwnerId);
        return bookingService.patchBookingById(itemOwnerId, bookingId, approved);
    }

}
