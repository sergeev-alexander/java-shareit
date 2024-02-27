package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.ValidationMarker;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getAllUsers(
            HttpServletRequest request,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(value = "size", defaultValue = "20") @Min(1) @Max(100) Integer size) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return userService.getAllUsers(PageRequest.of(page, size));
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(
            HttpServletRequest request,
            @PathVariable @Positive Long userId) {
        log.info("{} {}", request.getMethod(), request.getRequestURI());
        return userService.getUserDtoById(userId);
    }

    @PostMapping
    public UserDto postUser(
            HttpServletRequest request,
            @RequestBody @Validated(ValidationMarker.OnCreate.class) UserDto userDto) {
        log.info("{} {} {}", request.getMethod(), request.getRequestURI(), userDto);
        return userService.postUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto patchUserById(
            HttpServletRequest request,
            @PathVariable @Positive Long userId,
            @RequestBody @Validated(ValidationMarker.OnUpdate.class) UserDto userDto) {
        log.info("{} {} {}", request.getMethod(), request.getRequestURI(), userDto);
        return userService.patchUserById(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(
            HttpServletRequest request,
            @PathVariable @Positive Long userId) {
        log.info("{} {}", request.getMethod(), request.getRequestURI());
        userService.deleteUserById(userId);
    }

}
