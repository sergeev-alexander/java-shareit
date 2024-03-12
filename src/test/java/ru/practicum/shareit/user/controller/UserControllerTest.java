package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.exeption.ExceptionHandler;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UserController.class, ExceptionHandler.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @SneakyThrows
    void getAllUsers_whenValidFromAndSize_shouldInvokeUserServiceMethod_andReturnUser() {
        UserDto userDto = new UserDto(
                null,
                "Name",
                "name@email.com");
        when(userService.getAllUsers(PageRequest.of(0, 10)))
                .thenReturn(List.of(userDto));
        mockMvc.perform(get("/users?from=5&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(userDto))));
        verify(userService).getAllUsers(PageRequest.of(0, 10));
    }

    @Test
    @SneakyThrows
    void getAllUsers_whenNegativeFromAndNegativeSize_shouldNotInvokeUserServiceMethod_andThrowConstraintViolationException() {
        mockMvc.perform(get("/users?from=-1"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("getAllUsers.firstElement: " +
                                "must be greater than or equal to 0",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("{\"error\":\"must be greater than or equal to 0\"}"));
        verifyNoInteractions(userService);
    }

    @Test
    @SneakyThrows
    void getAllUsers_whenLessThan1Size_shouldNotInvokeUserServiceMethod_andThrowConstraintViolationException() {
        mockMvc.perform(get("/users?size=-1"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("getAllUsers.size: " +
                                "must be greater than or equal to 1",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("{\"error\":\"must be greater than or equal to 1\"}"));
        verifyNoInteractions(userService);
    }

    @Test
    @SneakyThrows
    void getAllUsers_whenGreaterThen20Size_shouldNotInvokeUserServiceMethod_andThrowConstraintViolationException() {
        mockMvc.perform(get("/users?size=21"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("getAllUsers.size: " +
                                "must be less than or equal to 20",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("{\"error\":\"must be less than or equal to 20\"}"));
        verifyNoInteractions(userService);
    }

    @Test
    @SneakyThrows
    void getUserById_whenValidId_shouldInvokeUserServiceMethod() {
        UserDto userDto = new UserDto(
                1L,
                "Name",
                "name@email.com");
        when(userService.getUserById(1L))
                .thenReturn(userDto);
        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userDto)));
        verify(userService).getUserById(1L);
    }

    @Test
    @SneakyThrows
    void getUserById_whenNegativeId_shouldNotInvokeUserServiceMethod_andThrowConstraintViolationException() {
        mockMvc.perform(get("/users/{id}", -1))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("getUserById.userId: " +
                                "must be greater than 0",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("{\"error\":\"must be greater than 0\"}"));
        verifyNoInteractions(userService);
    }

    @Test
    @SneakyThrows
    void postUser_whenValidUser_shouldInvokeUserServiceMethod() {
        UserDto userDto = new UserDto(
                null,
                "Name",
                "name@email.com");
        when(userService.postUser(userDto)).thenReturn(userDto);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userDto)));
        verify(userService).postUser(userDto);
    }

    @Test
    @SneakyThrows
    void postUser_whenNotValidUser_shouldNotInvokeUserServiceMethod_andThrowMethodArgumentNotValidException() {
        UserDto userDto = new UserDto(
                123L,
                "",
                "not an email");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof MethodArgumentNotValidException))
                .andExpect(content().string(containsString("\"name\":\"User name field is blank!" +
                        "\",\"id\":\"Creating user already has an id!\",\"email\":\"Wrong email format!\"")));
        verifyNoInteractions(userService);
    }

    @Test
    @SneakyThrows
    void patchUserById_whenValidUser_shouldInvokeUserServiceMethod() {
        UserDto userDto = new UserDto(
                null,
                "Name",
                "name@email.com");
        when(userService.patchUserById(1L, userDto))
                .thenReturn(userDto);
        mockMvc.perform(patch("/users/{id}", 1)
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userDto)));
        verify(userService).patchUserById(1L, userDto);
    }

    @Test
    @SneakyThrows
    void patchUserById_whenNegativeUserId_shouldNotInvokeUserServiceMethod_andThrowConstraintViolationException() {
        UserDto userDto = new UserDto(
                null,
                "Name",
                "name@email.com");
        mockMvc.perform(patch("/users/{id}", -1)
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("patchUserById.userId: " +
                                "must be greater than 0",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("{\"error\":\"must be greater than 0\"}"));
        verifyNoInteractions(userService);
    }

    @Test
    @SneakyThrows
    void patchUserById_whenNotValidUser_shouldNotInvokeUserServiceMethod_andThrowMethodArgumentNotValidException() {
        UserDto userDto = new UserDto(
                null,
                null,
                "not an email");
        mockMvc.perform(patch("/users/{id}", 1)
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof MethodArgumentNotValidException))
                .andExpect(content().string("{\"email\":\"Wrong email format!\"}"));
        verifyNoInteractions(userService);
    }

    @Test
    @SneakyThrows
    void deleteUserById_whenValidUserId_shouldInvokeUserServiceMethod() {
        mockMvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isOk());
        verify(userService, times(1)).deleteUserById(1L);
    }

    @Test
    @SneakyThrows
    void deleteUserById_whenNegativeUserId_shouldNotInvokeUserServiceMethod_andThrowConstraintViolationException() {
        mockMvc.perform(delete("/users/{id}", -1))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("deleteUserById.userId: " +
                                "must be greater than 0",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("{\"error\":\"must be greater than 0\"}"));
        verifyNoInteractions(userService);
    }

}
