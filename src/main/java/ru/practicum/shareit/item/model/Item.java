package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Item {

    private Long id;

    @NotBlank(message = "Item name field is blank!")
    private String name;

    @NotBlank(message = "Item description field is blank!")
    private String description;

    @NotNull(message = "Item available field is null!")
    private Boolean available;

    private User owner;

    private ItemRequest request;

}
