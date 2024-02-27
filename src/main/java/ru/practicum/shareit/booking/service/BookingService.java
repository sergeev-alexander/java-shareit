package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.dto.OutgoingBookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

public interface BookingService {

    Collection<OutgoingBookingDto> getAllUserBookings(Long userId, BookingState bookingState, Pageable pageable);

    Collection<OutgoingBookingDto> getAllOwnerItemBookings(Long ownerId, BookingState bookingState, Pageable pageable);

    OutgoingBookingDto getBookingById(Long userId, Long bookingId);

    OutgoingBookingDto postBooking(Long bookerId, IncomingBookingDto incomingBookingDto);

    OutgoingBookingDto patchBookingById(Long itemOwnerId, Long bookingId, Boolean approved);

}
