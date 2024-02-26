package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.LastNextBookingDto;

import java.util.List;

@Data
@AllArgsConstructor
public class OutgoingItemDto {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private LastNextBookingDto lastBooking;

    private LastNextBookingDto nextBooking;

    private List<OutgoingCommentDto> comments;

}
