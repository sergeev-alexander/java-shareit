package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    Collection<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto postUser(UserDto userDto);

    UserDto patchUserById(Long userId, UserDto userDto);

    void deleteUserById(Long userId);

}
