package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.exeption.ValidationMarker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
public class ItemDto {

    @Null(groups = ValidationMarker.OnCreate.class, message = "Creating item already has an id!")
    private Long id;

    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "Creating item name field is blank!")
    private String name;

    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "Creating item description field is blank!")
    private String description;

    @NotNull(groups = ValidationMarker.OnCreate.class, message = "Creating item available field is null!")
    private Boolean available;

}
