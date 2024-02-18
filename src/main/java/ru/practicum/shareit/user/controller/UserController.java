package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.ValidationMarker;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable @Positive Long userId) {
        return userService.getUserDtoById(userId);
    }

    @PostMapping
    public UserDto postUser(@RequestBody @Validated(ValidationMarker.OnCreate.class) UserDto userDto) {
        return userService.postUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto patchUserById(@PathVariable @Positive Long userId,
                                 @RequestBody @Validated(ValidationMarker.OnUpdate.class) UserDto userDto) {
        return userService.patchUserById(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable @Positive Long userId) {
        userService.deleteUserById(userId);
    }

}
