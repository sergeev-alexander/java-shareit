package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class IncomingCommentDto {

    private Long id;

    private String text;

}
