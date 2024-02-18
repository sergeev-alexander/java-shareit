package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.exeption.ValidationMarker;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;

    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "Creating comment text field is blank!")
    private String text;

    private String authorName;

    private LocalDateTime created = LocalDateTime.now();

}
