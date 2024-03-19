package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.exeption.ValidationMarker;
import ru.practicum.shareit.validation.BookingDateTimeValidation;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@BookingDateTimeValidation(groups = ValidationMarker.OnCreate.class)
public class IncomingBookingDto {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;

}
