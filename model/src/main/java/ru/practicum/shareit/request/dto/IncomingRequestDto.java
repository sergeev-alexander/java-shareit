package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.ValidationMarker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomingRequestDto {

    @Null(groups = ValidationMarker.OnCreate.class, message = "Creating request already has an id!")
    private Long id;

    @NotBlank(groups = ValidationMarker.OnCreate.class,
            message = "Creating request description field is blank!")
    @Size(groups = ValidationMarker.OnCreate.class, max = 128,
            message = "Creating request description field is bigger than 128 characters!")
    private String description;
}
