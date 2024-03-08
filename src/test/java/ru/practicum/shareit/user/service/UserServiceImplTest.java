package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepository;

    private final UserDto userDto = new UserDto(
            1L,
            "Some name",
            "some@email.com");

    private final User user = new User(
            1L,
            "Some name",
            "some@email.com");

    @Test
    void getAllUsers_whenCalled_shouldCallUserRepository_andReturnCollection() {
        when(userRepository.findBy(PageRequest.of(0, 20)))
                .thenReturn(List.of(user));
        Collection<UserDto> result = userServiceImpl.getAllUsers(PageRequest.of(0, 20));
        verify(userRepository).findBy(PageRequest.of(0, 20));
        assertNotNull(result);
        assertEquals(List.of(userDto), result);
    }

    @Test
    void getUserById_whenUserIsPresent_shouldCallUserRepository_andReturnUser() {
        when(userRepository.getUserById(1L))
                .thenReturn(user);
        UserDto result = userServiceImpl.getUserById(1L);
        verify(userRepository).getUserById(1L);
        assertNotNull(result);
        assertEquals(userDto, result);
    }

    @Test
    void getUserById_whenUserIsNotPresent_shouldCallUserRepository_andThrowNotFoundException() {
        when(userRepository.getUserById(1L))
                .thenThrow(new NotFoundException("There's no user with id 1"));
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> userServiceImpl.getUserById(1L));
        assertEquals("There's no user with id 1", notFoundException.getMessage());
    }

    @Test
    void postUser_whenCalled_shouldCallUserRepository_andReturnUser() {
        when(userRepository.save(user))
                .thenReturn(user);
        UserDto result = userServiceImpl.postUser(userDto);
        verify(userRepository).save(user);
        assertNotNull(result);
        assertEquals(userDto, result);
    }

    @Test
    void patchUserById_whenCalled_shouldCallUserRepository_andReturnUpdatedUser() {
        User oldUser = new User(
                1L,
                "Old name",
                "old@email.com");
        User expectedUser = new User(
                1L,
                "Updated name",
                "updated@email.com");
        when(userRepository.getUserById(1L))
                .thenReturn(oldUser);
        when(userRepository.save(any(User.class)))
                .thenReturn(expectedUser);
        User result = UserMapper.mapDtoToUser(userServiceImpl.patchUserById(1L, UserMapper.mapUserToDto(oldUser)));
        verify(userRepository).getUserById(1L);
        verify(userRepository).save(any(User.class));
        assertEquals(expectedUser, result);
    }

    @Test
    void deleteUserById_whenCalled_shouldCallUserRepositoryMethod() {
        userServiceImpl.deleteUserById(1L);
        verify(userRepository).deleteById(1L);
    }

}
