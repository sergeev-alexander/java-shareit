package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

@Component
public class BookingMapper {

    public static OutgoingBookingDto mapBookingToOutgoingDto(Booking booking) {
        return new OutgoingBookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker(),
                booking.getItem(),
                booking.getStatus());
    }

    public static Booking mapIncomingDtoToBooking(IncomingBookingDto incomingBookingDto) {
        return new Booking(
                incomingBookingDto.getId(),
                incomingBookingDto.getStart(),
                incomingBookingDto.getEnd(),
                null,
                null,
                BookingStatus.WAITING);
    }

    public static LastNextBookingDto mapBookingToLastNextDto(Booking booking) {
        return new LastNextBookingDto(
                booking.getId(),
                booking.getBooker().getId());
    }

}
