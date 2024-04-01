package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.ValidationMarker;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers(
            HttpServletRequest request,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", defaultValue = "20") @Min(1) @Max(20) Integer size) {
        log.info("{} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return userClient.getAllUsers(from, size);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(
            HttpServletRequest request,
            @PathVariable @Positive Long userId) {
        log.info("{} {}", request.getMethod(), request.getRequestURI());
        return userClient.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> postUser(
            HttpServletRequest request,
            @RequestBody @Validated(ValidationMarker.OnCreate.class) UserDto userDto) {
        log.info("{} {} {}", request.getMethod(), request.getRequestURI(), userDto);
        return userClient.postUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> patchUserById(
            HttpServletRequest request,
            @PathVariable @Positive Long userId,
            @RequestBody @Validated(ValidationMarker.OnUpdate.class) UserDto userDto) {
        log.info("{} {} {}", request.getMethod(), request.getRequestURI(), userDto);
        return userClient.patchUserById(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(
            HttpServletRequest request,
            @PathVariable @Positive Long userId) {
        log.info("{} {}", request.getMethod(), request.getRequestURI());
        return userClient.deleteUserById(userId);
    }

}
