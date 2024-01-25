package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemServiceImplementation;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImplementation;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserServiceImplementation userService;
    private final ItemServiceImplementation itemService;

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable @Positive Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDto postUser(@RequestBody @Valid User user) {
        return userService.postUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto patchUserById(@PathVariable @Positive Long userId, @RequestBody User user) {
        return userService.patchUserById(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable @Positive Long userId) {
        itemService.deleteAllOwnerItems(userId);
        userService.deleteUserById(userId);
    }

}
