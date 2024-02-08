package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class Booking {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private User booker;

    private Status status;

}
