package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class User {

    private Long id;

    @NotBlank(message = "User name field is blank!")
    private String name;

    @NotBlank(message = "Email field is blank!")
    @Email(message = "Wrong email format!")
    private String email;

}
