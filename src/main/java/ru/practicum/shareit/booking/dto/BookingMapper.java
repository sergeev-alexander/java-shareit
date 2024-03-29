package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

@UtilityClass
public class BookingMapper {

    public OutgoingBookingDto mapBookingToOutgoingDto(Booking booking) {
        return new OutgoingBookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker(),
                booking.getItem(),
                booking.getStatus());
    }

    public Booking mapIncomingDtoToBooking(IncomingBookingDto incomingBookingDto) {
        return new Booking(
                incomingBookingDto.getId(),
                incomingBookingDto.getStart(),
                incomingBookingDto.getEnd(),
                null,
                null,
                BookingStatus.WAITING);
    }

    public LastNextBookingDto mapBookingToLastNextDto(Booking booking) {
        return new LastNextBookingDto(
                booking.getId(),
                booking.getBooker().getId());
    }

}
