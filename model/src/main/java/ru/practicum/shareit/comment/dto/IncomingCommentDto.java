package ru.practicum.shareit.comment.dto;

import lombok.*;
import ru.practicum.shareit.validation.ValidationMarker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class IncomingCommentDto {

    @Null(groups = ValidationMarker.OnCreate.class, message = "Creating comment already has an id!")
    private Long id;

    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "Creating comment text field is blank!")
    @Size(groups = ValidationMarker.OnCreate.class, max = 128,
            message = "Creating comment text field is bigger than 128 characters!")
    private String text;

}
