package ru.practicum.shareit.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers(Pageable pageable);

    UserDto getUserById(Long userId);

    UserDto postUser(UserDto userDto);

    UserDto patchUserById(Long userId, UserDto userDto);

    void deleteUserById(Long userId);

}
