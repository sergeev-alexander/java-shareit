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
import ru.practicum.shareit.exeption.ExceptionResolver;
import ru.practicum.shareit.request.dto.IncomingRequestDto;
import ru.practicum.shareit.request.dto.OutgoingRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
        mockMvc.perform(get("/requests?from=0&size=20")
                        .header(header, 1))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(outgoingRequestDto))));
        verify(requestService).getAllRequesterRequests(1L,
                PageRequest.of(0, 20, sortByCreatingDesc));
    }

    @Test
    @SneakyThrows
    void getAllRequests_whenInvoke_shouldInvokeRequestServiceMethod() {
        when(requestService.getAllRequests(1L, PageRequest.of(0, 20, sortByCreatingDesc)))
                .thenReturn(List.of(outgoingRequestDto));
        mockMvc.perform(get("/requests/all?from=0&size=20")
                        .header(header, 1))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(outgoingRequestDto))));
        verify(requestService).getAllRequests(1L, PageRequest.of(0, 20, sortByCreatingDesc));
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

}
