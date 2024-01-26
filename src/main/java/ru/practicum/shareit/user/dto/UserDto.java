package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.exeption.ValidationMarker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
public class UserDto {

    @Null(groups = ValidationMarker.OnCreate.class, message = "Creating user already has an id!")
    private Long id;

    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "User name field is blank!")
    private String name;

    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "Email field is blank!")
    @Email(message = "Wrong email format!")
    private String email;

}
