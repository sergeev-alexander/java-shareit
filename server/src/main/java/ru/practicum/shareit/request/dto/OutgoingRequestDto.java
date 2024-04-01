package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.OutgoingItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutgoingRequestDto {

    private Long id;

    private String description;

    private LocalDateTime created;

    private Long requesterId;

    private List<OutgoingItemDto> items;

}
