package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
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
import ru.practicum.shareit.exeption.ExceptionResolver;
import ru.practicum.shareit.request.dto.IncomingRequestDto;
import ru.practicum.shareit.request.dto.OutgoingRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.http.HttpHeader.header;

@WebMvcTest({RequestController.class, ExceptionResolver.class})
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestService requestService;
    private final Sort sortByCreatingDesc = Sort.by(Sort.Direction.DESC, "created");
    private OutgoingRequestDto outgoingRequestDto;

    @BeforeEach
    void setOutgoingRequestDto() {
        outgoingRequestDto = new OutgoingRequestDto(
                1L,
                "Some description",
                LocalDateTime.of(2000, 1, 1, 1, 1, 1),
                1L,
                null);
    }

    @Test
    @SneakyThrows
    void getAllRequesterRequests_whenInvoke_shouldInvokeRequestServiceMethod() {
        when(requestService.getAllRequesterRequests(1L,
                PageRequest.of(0, 20, sortByCreatingDesc)))
                .thenReturn(List.of(outgoingRequestDto));
        mockMvc.perform(get("/requests")
                        .header(header, 1))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(outgoingRequestDto))));
        verify(requestService).getAllRequesterRequests(1L,
                PageRequest.of(0, 20, sortByCreatingDesc));
    }

    @Test
    @SneakyThrows
    void getAllRequesterRequests_whenWrongHeader_shouldNotInvokeRequestServiceMethod_andThrowMissingRequestHeaderException() {
        mockMvc.perform(get("/requests")
                        .header("WRONG-HEADER", "WRONG-VALUE"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof MissingRequestHeaderException))
                .andExpect(result -> assertEquals("Required request header 'X-Sharer-User-Id' " +
                                "for method parameter type Long is not present",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("MissingRequestHeaderException : " +
                        "Required request header 'X-Sharer-User-Id' for method parameter " +
                        "type Long is not present"));
        verifyNoInteractions(requestService);
    }

    @Test
    @SneakyThrows
    void getAllRequesterRequests_whenNotValidFromAndSize_shouldThrowConstraintViolationException() {
        mockMvc.perform(get("/requests?from=-1&size=0")
                        .header(header, 1))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> assertTrue(result
                        .getResolvedException().getMessage()
                        .contains("getAllRequesterRequests.size: must be greater than or equal to 1")))
                .andExpect(result -> assertTrue(result
                        .getResolvedException().getMessage()
                        .contains("getAllRequesterRequests.firstElement: must be greater than or equal to 0")))
                .andExpect(content().string(containsString("error\":\"must be greater than or equal")));
        verifyNoInteractions(requestService);
    }

    @Test
    @SneakyThrows
    void getAllRequests_whenInvoke_shouldInvokeRequestServiceMethod() {
        when(requestService.getAllRequests(1L, PageRequest.of(0, 20, sortByCreatingDesc)))
                .thenReturn(List.of(outgoingRequestDto));
        mockMvc.perform(get("/requests/all")
                        .header(header, 1))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(outgoingRequestDto))));
        verify(requestService).getAllRequests(1L, PageRequest.of(0, 20, sortByCreatingDesc));
    }

    @Test
    @SneakyThrows
    void getAllRequests_whenNotValidFromAndSize_shouldThrowConstraintViolationException() {
        mockMvc.perform(get("/requests/all?from=-1&size=0")
                        .header(header, 1))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException))
                .andExpect(result -> assertTrue(result
                        .getResolvedException().getMessage()
                        .contains("getAllRequests.size: must be greater than or equal to 1")))
                .andExpect(result -> assertTrue(result
                        .getResolvedException().getMessage()
                        .contains("getAllRequests.firstElement: must be greater than or equal to 0")))
                .andExpect(content().string(containsString("error\":\"must be greater than or equal")));
        verifyNoInteractions(requestService);
    }

    @Test
    @SneakyThrows
    void getAllRequests_whenNoRequestHeader_shouldThrowMissingRequestHeaderException() {
        mockMvc.perform(get("/requests/all?from=0&size=1"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof MissingRequestHeaderException))
                .andExpect(result -> assertEquals(
                        "Required request header 'X-Sharer-User-Id' " +
                                "for method parameter type Long is not present",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("MissingRequestHeaderException : " +
                        "Required request header 'X-Sharer-User-Id' for method parameter " +
                        "type Long is not present"));
        verifyNoInteractions(requestService);
    }

    @Test
    @SneakyThrows
    void getRequestById_whenInvoke_shouldInvokeRequestServiceMethod() {
        when(requestService.getRequestById(1L, 1L))
                .thenReturn(outgoingRequestDto);
        mockMvc.perform(get("/requests/{id}", 1)
                        .header(header, 1))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(outgoingRequestDto)));
        verify(requestService).getRequestById(1L, 1L);
    }

    @Test
    @SneakyThrows
    void getRequestById_whenNegativeRequestId_shouldThrowConstraintViolationException() {
        mockMvc.perform(get("/requests/{id}", -1)
                        .header(header, 1))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("getRequestById.requestId: must be greater than 0",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("{\"error\":\"must be greater than 0\"}"));
        verifyNoInteractions(requestService);
    }

    @Test
    @SneakyThrows
    void postRequest_whenInvoke_shouldInvokeRequestServiceMethod() {
        IncomingRequestDto incomingRequestDto = new IncomingRequestDto(
                null,
                "Some description");
        when(requestService.postRequest(1L, incomingRequestDto))
                .thenReturn(outgoingRequestDto);
        mockMvc.perform(post("/requests")
                        .header(header, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(outgoingRequestDto)));
        verify(requestService).postRequest(1L, incomingRequestDto);
    }

    @Test
    @SneakyThrows
    void postRequest_whenNotValidRequest_shouldThrowMethodArgumentNotValidException() {
        IncomingRequestDto incomingRequestDto = new IncomingRequestDto(
                1L,
                "");
        mockMvc.perform(post("/requests")
                        .header(header, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(content().string(containsString("id\":\"" +
                        "Creating request already has an id!")))
                .andExpect(content().string(containsString("description\":\"" +
                        "Creating request description field is blank!")));
        verifyNoInteractions(requestService);
    }

    @Test
    @SneakyThrows
    void postRequest_whenWrongRequestHeader_shouldThrowMissingRequestHeaderException() {
        IncomingRequestDto incomingRequestDto = new IncomingRequestDto(
                null,
                "Some description");
        mockMvc.perform(post("/requests")
                        .header("WRONG-HEADER", 123)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof MissingRequestHeaderException))
                .andExpect(result -> assertEquals(
                        "Required request header 'X-Sharer-User-Id' " +
                                "for method parameter type Long is not present",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("MissingRequestHeaderException : " +
                        "Required request header 'X-Sharer-User-Id' for method parameter " +
                        "type Long is not present"));
        verifyNoInteractions(requestService);
    }

}
