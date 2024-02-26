package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Collection<UserDto> getAllUsers() {
        return userRepository.findBy(UserDto.class);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId, User.class)
                .orElseThrow(() -> new NotFoundException("There's no user with id " + userId));
    }

    public UserDto getUserDtoById(Long userId) {
        return userRepository.findById(userId, UserDto.class)
                .orElseThrow(() -> new NotFoundException("There's no user with id " + userId));
    }

    @Override
    public UserDto postUser(UserDto userDto) {
        return userMapper.mapUserToDto(userRepository.save(userMapper.mapDtoToUser(userDto)));
    }

    @Override
    public UserDto patchUserById(Long userId, UserDto userDto) {
        User updatingUser = getUserById(userId);
        if (null != userDto.getName() && !userDto.getName().isBlank()) {
            updatingUser.setName(userDto.getName());
        }
        if (null != userDto.getEmail() && !userDto.getEmail().isBlank()) {
            updatingUser.setEmail(userDto.getEmail());
        }
        return userMapper.mapUserToDto(userRepository.save(updatingUser));
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

}
