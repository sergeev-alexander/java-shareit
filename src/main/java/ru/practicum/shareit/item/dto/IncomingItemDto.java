package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.exeption.ValidationMarker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class IncomingItemDto {

    @NotBlank(groups = ValidationMarker.OnCreate.class,
        message = "Creating item name field is blank!")
    @Size(groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class}, max = 128,
            message = "Creating item name field is bigger than 128 characters!")
    private String name;

    @NotBlank(groups = ValidationMarker.OnCreate.class,
            message = "Creating item description field is blank!")
    @Size(groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class}, max = 128,
            message = "Creating item name field is bigger than 128 characters!")
    private String description;

    @NotNull(groups = ValidationMarker.OnCreate.class,
            message = "Creating item available field is null!")
    private Boolean available;

}
