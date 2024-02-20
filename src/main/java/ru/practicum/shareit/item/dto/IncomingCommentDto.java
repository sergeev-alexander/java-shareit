package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.exeption.ValidationMarker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class IncomingCommentDto {

    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "Creating comment text field is blank!")
    @Size(groups = ValidationMarker.OnCreate.class, max = 128,
            message = "Creating comment text field is bigger than 128 characters!")
    private String text;

}
