package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OutgoingBookingDto {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private User booker;

    private Item item;

    private BookingStatus status;

}
