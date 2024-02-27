package ru.practicum.shareit.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {

    Collection<UserDto> getAllUsers(Pageable pageable);

    UserDto getUserDtoById(Long userId);

    User getUserById(Long userId);

    UserDto postUser(UserDto userDto);

    UserDto patchUserById(Long userId, UserDto userDto);

    void deleteUserById(Long userId);

}
