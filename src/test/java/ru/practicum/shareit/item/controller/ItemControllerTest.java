package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import ru.practicum.shareit.exeption.ExceptionHandler;
import ru.practicum.shareit.item.dto.IncomingCommentDto;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.OutgoingCommentDto;
import ru.practicum.shareit.item.dto.OutgoingItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ItemController.class, ExceptionHandler.class})
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;
    private final String header = "X-Sharer-User-Id";
    private final Sort sortByStartAsc = Sort.by(Sort.Direction.ASC, "start");

    @Test
    @SneakyThrows
    void getAllOwnerItems_whenValidFromAndSize_shouldCallItemServiceMethod() {
        OutgoingItemDto outgoingItemDto = new OutgoingItemDto(
                1L,
                "Item name",
                "Item description",
                true,
                null,
                null,
                List.of(),
                2L);
        when(itemService.getAllOwnerItems(3L, PageRequest.of(0, 20, sortByStartAsc)))
                .thenReturn(List.of(outgoingItemDto));
        mockMvc.perform(get("/items?from=0&size=20")
                        .header(header, 3))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(outgoingItemDto))));
        verify(itemService).getAllOwnerItems(3L, PageRequest.of(0, 20, sortByStartAsc));
    }

    @Test
    @SneakyThrows
    void getAllOwnerItems_whenNotValidFromAndSize_shouldThrowConstraintViolationException() {
        mockMvc.perform(get("/items?from=-1&size=0")
                        .header(header, 1))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException))
                .andExpect(result -> assertTrue(result
                        .getResolvedException().getMessage()
                        .contains("getAllOwnerItems.size: must be greater than or equal to 1")))
                .andExpect(result -> assertTrue(result
                        .getResolvedException().getMessage()
                        .contains("getAllOwnerItems.firstElement: must be greater than or equal to 0")))
                .andExpect(content().string(containsString("error\":\"must be greater than or equal")));
        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void getAllOwnerItems_whenWrongRequestHeader_shouldThrowMissingRequestHeaderException() {
        mockMvc.perform(get("/items?from=0&size=1")
                        .header("WRONG-HEADER", "WRONG-VALUE"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException()
                                instanceof MissingRequestHeaderException))
                .andExpect(result -> assertEquals("Required request header 'X-Sharer-User-Id' " +
                                "for method parameter type Long is not present",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("MissingRequestHeaderException : " +
                        "Required request header 'X-Sharer-User-Id' for method parameter " +
                        "type Long is not present"));
        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void getItemById_whenValidRequestHeaderAndItemId_shouldInvokeItemServiceMethod() {
        OutgoingItemDto outgoingItemDto = new OutgoingItemDto(
                1L,
                "Item name",
                "Item description",
                true,
                null,
                null,
                List.of(),
                2L);
        when(itemService.getItemDtoById(3L, 1L))
                .thenReturn(outgoingItemDto);
        mockMvc.perform(get("/items/{id}", 1)
                        .header(header, 3))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(outgoingItemDto)));
        verify(itemService).getItemDtoById(3L, 1L);
    }

    @Test
    @SneakyThrows
    void getItemById_whenWrongRequestHeader_shouldThrowMissingRequestHeaderException() {
        mockMvc.perform(get("/items/{id}", 1)
                        .header("WRONG-HEADER", "WRONG-VALUE"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException()
                                instanceof MissingRequestHeaderException))
                .andExpect(result -> assertEquals("Required request header 'X-Sharer-User-Id' " +
                                "for method parameter type Long is not present",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("MissingRequestHeaderException : " +
                        "Required request header 'X-Sharer-User-Id' for method parameter " +
                        "type Long is not present"));
        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void getItemById_whenNegativeItemId_shouldThrowConstraintViolationException() {
        mockMvc.perform(get("/items/{id}", -1)
                        .header(header, 1))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("getItemById.itemId: must be greater than 0",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("{\"error\":\"must be greater than 0\"}"));
        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void getItemsBySearch_whenValidRequestHeaderAndUserId_shouldInvokeItemServiceMethod() {
        OutgoingItemDto outgoingItemDto = new OutgoingItemDto(
                1L,
                "Item name",
                "Item description",
                true,
                null,
                null,
                List.of(),
                2L);
        when(itemService.getItemsBySearch(3L, "SomeText", PageRequest.of(0, 20)))
                .thenReturn(List.of(outgoingItemDto));
        mockMvc.perform(get("/items/search?from=0&size=20&text=SomeText")
                        .header(header, 3))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(outgoingItemDto))));
        verify(itemService).getItemsBySearch(3L, "SomeText", PageRequest.of(0, 20));
    }

    @Test
    @SneakyThrows
    void getItemsBySearch_whenNegativeRequestHeader_shouldThrowConstraintViolationException() {
        mockMvc.perform(get("/items/search?text=SomeText")
                        .header(header, -1))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("getItemsBySearch.userId: must be greater than 0",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("{\"error\":\"must be greater than 0\"}"));
        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void postItem_whenValidItemAndRequestHeader_shouldInvokeItemServiceMethod() {
        IncomingItemDto incomingItemDto = new IncomingItemDto(
                "Some Name",
                "Some description",
                true,
                null);
        OutgoingItemDto outgoingItemDto = new OutgoingItemDto(
                1L,
                "Some Name",
                "Some description",
                true,
                null,
                null,
                List.of(),
                2L);
        when(itemService.postItem(1L, incomingItemDto))
                .thenReturn(outgoingItemDto);
        mockMvc.perform(post("/items")
                        .header(header, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingItemDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(outgoingItemDto)));
        verify(itemService).postItem(1L, incomingItemDto);
    }

    @Test
    @SneakyThrows
    void postItem_whenWrongRequestHeader_shouldThrowCMissingRequestHeaderException() {
        IncomingItemDto incomingItemDto = new IncomingItemDto(
                "Some Name",
                "Some description",
                true,
                null);
        mockMvc.perform(post("/items")
                        .header("WRONG-HEADER", "WRONG-VALUE")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingItemDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MissingRequestHeaderException))
                .andExpect(result -> assertEquals("Required request header 'X-Sharer-User-Id' " +
                                "for method parameter type Long is not present",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("MissingRequestHeaderException : " +
                        "Required request header 'X-Sharer-User-Id' for method parameter " +
                        "type Long is not present"));
        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void postItem_whenNotValidItem_shouldThrowMethodArgumentNotValidException() {
        IncomingItemDto incomingItemDto = new IncomingItemDto(
                "",
                null,
                null,
                -1L);
        mockMvc.perform(post("/items")
                        .header(header, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingItemDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(content().string(containsString("requestId\":\"" +
                        "Creating item requestId field must be positive!")))
                .andExpect(content().string(containsString("name\":\"" +
                        "Creating item name field is blank!")))
                .andExpect(content().string(containsString("available\":\"" +
                        "Creating item available field is null!")))
                .andExpect(content().string(containsString("description\":\"" +
                        "Creating item description field is blank!")));
        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void postComment_whenValidComment_shouldInvokeItemServiceMethod() {
        IncomingCommentDto incomingCommentDto = new IncomingCommentDto(
                null,
                "Some text");
        OutgoingCommentDto outgoingCommentDto = new OutgoingCommentDto(
                1L,
                "Some text",
                "Some author name",
                LocalDateTime.of(2000, 1, 1, 1, 1, 1));
        when(itemService.postComment(1L, 1L, incomingCommentDto))
                .thenReturn(outgoingCommentDto);
        mockMvc.perform(post("/items/{id}/comment", 1L)
                        .header(header, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingCommentDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(outgoingCommentDto)));
        verify(itemService).postComment(1L, 1L, incomingCommentDto);
    }

    @Test
    @SneakyThrows
    void postComment_whenNotValidComment_shouldThrowMethodArgumentNotValidException() {
        IncomingCommentDto incomingCommentDto = new IncomingCommentDto(
                1L,
                "");
        mockMvc.perform(post("/items/{id}/comment", 1L)
                        .header(header, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingCommentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(content().string(containsString("text\":\"" +
                        "Creating comment text field is blank!")))
                .andExpect(content().string(containsString("id\":\"" +
                        "Creating comment already has an id!")));
        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void postComment_whenNegativeItemId_shouldThrowConstraintViolationException() {
        IncomingCommentDto incomingCommentDto = new IncomingCommentDto(
                null,
                "Some text");
        mockMvc.perform(post("/items/{id}/comment", -1)
                        .header(header, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingCommentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("postComment.itemId: must be greater than 0",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("{\"error\":\"must be greater than 0\"}"));
        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void patchItemById_whenValidItem_shouldInvokeItemServiceMethod() {
        IncomingItemDto incomingItemDto = new IncomingItemDto(
                "Some name",
                "Some description",
                true,
                1L);
        OutgoingItemDto outgoingItemDto = new OutgoingItemDto(
                1L,
                "Some name",
                "Some description",
                true,
                null,
                null,
                List.of(),
                1L);
        when(itemService.patchItemById(1L, 1L, incomingItemDto))
                .thenReturn(outgoingItemDto);
        mockMvc.perform(patch("/items/{id}", 1L)
                        .header(header, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingItemDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(outgoingItemDto)));
        verify(itemService).patchItemById(1L, 1L, incomingItemDto);
    }

    @Test
    @SneakyThrows
    void patchItemById_whenNotValidItem_shouldThrowMethodArgumentNotValidException() {
        IncomingItemDto incomingItemDto = new IncomingItemDto(
                "1234567890123456789012345678901234567890123456789012345678901234567890" +
                        "123456789012345678901234567890123456789012345678901234567890",
                "1234567890123456789012345678901234567890123456789012345678901234567890" +
                        "123456789012345678901234567890123456789012345678901234567890",
                null,
                null);
        mockMvc.perform(patch("/items/{id}", 1L)
                        .header(header, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingItemDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(content().string(containsString("name\":\"" +
                        "Creating item name field is bigger than 128 characters!")))
                .andExpect(content().string(containsString("description\":\"" +
                        "Creating item description field is bigger than 128 characters!")));
        verifyNoInteractions(itemService);
    }

    @Test
    @SneakyThrows
    void patchItemById_whenNegativeUserId_shouldThrowConstraintViolationException() {
        IncomingItemDto incomingItemDto = new IncomingItemDto(
                "Some name",
                "Some description",
                true,
                1L);
        mockMvc.perform(patch("/items/{id}", 1)
                        .header(header, -1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingItemDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("patchItemById.ownerId: must be greater than 0",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("{\"error\":\"must be greater than 0\"}"));
        verifyNoInteractions(itemService);
    }


    @Test
    @SneakyThrows
    void deleteItemById_whenValidItemId_shouldInvokeItemServiceMethod() {
        mockMvc.perform(delete("/items/{id}", 1)
                .header(header, 1))
                .andExpect(status().isOk());
        verify(itemService).deleteItemById(1L, 1L);
    }

    @Test
    @SneakyThrows
    void deleteItemById_whenNegativeItemId_shouldThrowConstraintViolationException() {
        mockMvc.perform(delete("/items/{id}", -1)
                        .header(header, 1))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("deleteItemById.itemId: must be greater than 0",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("{\"error\":\"must be greater than 0\"}"));
        verifyNoInteractions(itemService);
    }

}
