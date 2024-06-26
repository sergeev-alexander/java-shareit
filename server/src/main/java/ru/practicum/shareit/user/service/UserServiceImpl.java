package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findBy(pageable)
                .stream()
                .map(UserMapper::mapUserToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.mapUserToDto(userRepository.getUserById(userId));
    }

    @Override
    public UserDto postUser(UserDto userDto) {
        return UserMapper.mapUserToDto(userRepository.save(UserMapper.mapDtoToUser(userDto)));
    }

    @Override
    public UserDto patchUserById(Long userId, UserDto userDto) {
        User updatingUser = UserMapper.mapDtoToUser(getUserById(userId));
        if (null != userDto.getName() && !userDto.getName().isBlank()) {
            updatingUser.setName(userDto.getName());
        }
        if (null != userDto.getEmail() && !userDto.getEmail().isBlank()) {
            updatingUser.setEmail(userDto.getEmail());
        }
        return UserMapper.mapUserToDto(userRepository.save(updatingUser));
    }

    @Override
    public UserDto deleteUserById(Long userId) {
        UserDto userDto = UserMapper.mapUserToDto(userRepository.getUserById(userId));
        userRepository.deleteById(userId);
        return userDto;
    }

}
