package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.ValidationMarker;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable @Positive Long userId) {
        return userService.getUserById(userId);
    }

    @Validated({ValidationMarker.OnCreate.class})
    @PostMapping
    public UserDto postUser(@RequestBody @Valid UserDto userDto) {
        return userService.postUser(userDto);
    }

    @Validated({ValidationMarker.OnUpdate.class})
    @PatchMapping("/{userId}")
    public UserDto patchUserById(@PathVariable @Positive Long userId, @RequestBody @Valid UserDto userDto) {
        return userService.patchUserById(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable @Positive Long userId) {
        userService.deleteUserById(userId);
    }

}
