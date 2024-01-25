package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {

    Collection<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto postUser(User user);

    UserDto patchUserById(Long userId, User user);

    void deleteUserById(Long userId);

}
