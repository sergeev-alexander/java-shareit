package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation {

    private final UserDao userDao;
    private final UserMapper userMapper;

    public Collection<UserDto> getAllUsers() {
        return userDao.getAllUsers().stream()
                .map(userMapper::mapUserToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long userId) {
        return userMapper.mapUserToDto(userDao.getUserById(userId));
    }

    public UserDto postUser(User user) {
        return userMapper.mapUserToDto(userDao.postUser(user));
    }

    public UserDto patchUserById(Long userId, User user) {
        return userMapper.mapUserToDto(userDao.patchUserById(userId, user));
    }

    public void deleteUserById(Long userId) {
        userDao.deleteUserById(userId);
    }

}
