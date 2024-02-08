package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final ItemDao itemDao;
    private final UserMapper userMapper;

    public Collection<UserDto> getAllUsers() {
        return userDao.getAllUsers().stream()
                .map(userMapper::mapUserToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userMapper.mapUserToDto(userDao.getUserById(userId));
    }

    @Override
    public UserDto postUser(UserDto userDto) {
        return userMapper.mapUserToDto(userDao.postUser(userMapper.mapDtoToUser(userDto)));
    }

    @Override
    public UserDto patchUserById(Long userId, UserDto userDto) {
        return userMapper.mapUserToDto(userDao.patchUserById(userId, userMapper.mapDtoToUser(userDto)));
    }

    @Override
    public void deleteUserById(Long userId) {
        itemDao.deleteAllOwnerItems(userId);
        userDao.deleteUserById(userId);
    }

}
