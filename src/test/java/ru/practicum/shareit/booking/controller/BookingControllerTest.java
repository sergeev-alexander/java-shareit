package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.dto.OutgoingBookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exeption.ExceptionHandler;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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

@WebMvcTest({BookingController.class, ExceptionHandler.class})
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;
    private final String header = "X-Sharer-User-Id";
    private final Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
    private final OutgoingBookingDto outgoingBookingDto = new OutgoingBookingDto(
            1L,
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now().plusDays(1),
            new User(
                    2L,
                    "Booker name",
                    "booker@email.com"),
            new Item(
                    1L,
                    "Item name",
                    "Item description",
                    true,
                    null,
                    new User(
                            1L,
                            "Owner name",
                            "owner@email.com")),
            BookingStatus.WAITING);

    @Test
    @SneakyThrows
    void getAllUserBookings_whenAllParamsAreValid_shouldInvokeServiceMethod_andReturnBookingList() {
        when(bookingService.getAllUserBookings(2L, BookingState.ALL,
                PageRequest.of(0, 20, sortByStartDesc)))
                .thenReturn(List.of(outgoingBookingDto));
        mockMvc.perform(get("/bookings?state=ALL&from=0&size=20")
                        .header(header, 2))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(outgoingBookingDto))));
        verify(bookingService).getAllUserBookings(2L, BookingState.ALL,
                PageRequest.of(0, 20, sortByStartDesc));
    }

    @Test
    @SneakyThrows
    void getAllUserBookings_whenNotValidRequestHeader_shouldThrowMissingRequestHeaderException() {
        mockMvc.perform(get("/bookings?state=ALL&from=0&size=20")
                        .header("WRONG-HEADER", "WRONG-VALUE"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MissingRequestHeaderException))
                .andExpect(result -> assertEquals("Required request header 'X-Sharer-User-Id' " +
                                "for method parameter type Long is not present",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("MissingRequestHeaderException : " +
                        "Required request header 'X-Sharer-User-Id' for method parameter " +
                        "type Long is not present"));
        verifyNoInteractions(bookingService);
    }

    @Test
    @SneakyThrows
    void getAllUserBookings_whenNotValidFromAndSize_shouldThrowConstraintViolationException() {
        mockMvc.perform(get("/bookings?from=-1&size=0")
                        .header(header, 2))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException))
                .andExpect(result -> assertTrue(result
                        .getResolvedException().getMessage()
                        .contains("getAllUserBookings.size: must be greater than or equal to 1")))
                .andExpect(result -> assertTrue(result
                        .getResolvedException().getMessage()
                        .contains("getAllUserBookings.firstElement: must be greater than or equal to 0")))
                .andExpect(content().string(containsString("error\":\"must be greater than or equal")));
        verifyNoInteractions(bookingService);
    }

    @Test
    @SneakyThrows
    void getAllUserBookings_whenWrongState_shouldThrowConstraintViolationException() {
        mockMvc.perform(get("/bookings?state=WRONG-STATE")
                        .header(header, 2))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("getAllUserBookings.bookingStateString.state: " +
                                "Unknown state: UNSUPPORTED_STATUS",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("{\"error\":\"Unknown state: UNSUPPORTED_STATUS\"}"));
        verifyNoInteractions(bookingService);
    }

    @Test
    @SneakyThrows
    void getAllOwnerItemBookings_whenAllParamsAreValid_shouldInvokeServiceMethod_andReturnBookingList() {
        when(bookingService.getAllOwnerItemBookings(1L, BookingState.ALL,
                PageRequest.of(0, 20, sortByStartDesc)))
                .thenReturn(List.of(outgoingBookingDto));
        mockMvc.perform(get("/bookings/owner?state=ALL&from=0&size=20")
                        .header(header, 1))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(outgoingBookingDto))));
        verify(bookingService).getAllOwnerItemBookings(1L, BookingState.ALL,
                PageRequest.of(0, 20, sortByStartDesc));
    }

    @Test
    @SneakyThrows
    void getAllOwnerItemBookings_whenNegativeHeader_shouldThrowConstraintViolationException() {
        mockMvc.perform(get("/bookings/owner?state=ALL&from=0&size=20", -1)
                        .header(header, -1))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("getAllOwnerItemBookings.ownerId: must be greater than 0",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("{\"error\":\"must be greater than 0\"}"));
        verifyNoInteractions(bookingService);
    }

    @Test
    @SneakyThrows
    void getBookingById_whenAllParamsAreValid_shouldInvokeServiceMethod_andReturnBooking() {
        when(bookingService.getBookingById(2L, 1L))
                .thenReturn(outgoingBookingDto);
        mockMvc.perform(get("/bookings/{id}", 1)
                        .header(header, 2))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(outgoingBookingDto)));
        verify(bookingService).getBookingById(2L, 1L);
    }

    @Test
    @SneakyThrows
    void getBookingById_whenNegativeBookingId_shouldThrowConstraintViolationException() {
        mockMvc.perform(get("/bookings/{id}", -1)
                        .header(header, 1))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("getBookingById.bookingId: must be greater than 0",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("{\"error\":\"must be greater than 0\"}"));
        verifyNoInteractions(bookingService);
    }

    @Test
    @SneakyThrows
    void postBooking_whenValidBooking_shouldInvokeServiceMethod_andReturnBooking() {
        IncomingBookingDto incomingBookingDto = new IncomingBookingDto(
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                1L);
        when(bookingService.postBooking(2L, incomingBookingDto))
                .thenReturn(outgoingBookingDto);
        mockMvc.perform(post("/bookings")
                        .header(header, 2)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingBookingDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(outgoingBookingDto)));
        verify(bookingService).postBooking(2L, incomingBookingDto);
    }

    @Test
    @SneakyThrows
    void postBooking_whenStartIsInPast_shouldThrow_shouldThrowMethodArgumentNotValidException() {
        IncomingBookingDto incomingBookingDto = new IncomingBookingDto(
                null,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                1L);
        mockMvc.perform(post("/bookings")
                        .header(header, 2)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingBookingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage()
                        .contains("Creating booking start is in past!")))
                .andExpect(content().string(
                        containsString("Creating booking start is in past!")));
        verifyNoInteractions(bookingService);
    }

    @Test
    @SneakyThrows
    void postBooking_whenEndIsInPast_shouldThrow_shouldThrowMethodArgumentNotValidException() {
        IncomingBookingDto incomingBookingDto = new IncomingBookingDto(
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().minusDays(1),
                1L);
        mockMvc.perform(post("/bookings")
                        .header(header, 2)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingBookingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage()
                        .contains("Creating booking end is in past!")))
                .andExpect(content().string(
                        containsString("Creating booking end is in past!")));
        verifyNoInteractions(bookingService);
    }

    @Test
    @SneakyThrows
    void postBooking_whenEndIsBeforeStart_shouldThrow_shouldThrowMethodArgumentNotValidException() {
        IncomingBookingDto incomingBookingDto = new IncomingBookingDto(
                null,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(1),
                1L);
        mockMvc.perform(post("/bookings")
                        .header(header, 2)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingBookingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage()
                        .contains("Creating booking start is before end!")))
                .andExpect(content().string("{\"start\":\"Creating booking start is before end!\"}"));
        verifyNoInteractions(bookingService);
    }

    @Test
    @SneakyThrows
    void patchBooking_whenValidParams_shouldInvokeServiceMethod_andReturnBooking() {
        when(bookingService.patchBookingById(1L, 1L, true))
                .thenReturn(outgoingBookingDto);
        mockMvc.perform(patch("/bookings/{id}", 1)
                        .header(header, 1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(outgoingBookingDto)));
        verify(bookingService).patchBookingById(1L, 1L, true);
    }

    @Test
    @SneakyThrows
    void patchBooking_whenNegativeBookingId_shouldThrowConstraintViolationException() {
        mockMvc.perform(patch("/bookings/{id}", -1)
                        .header(header, 1)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("patchBooking.bookingId: must be greater than 0",
                        result.getResolvedException().getMessage()))
                .andExpect(content().string("{\"error\":\"must be greater than 0\"}"));
        verifyNoInteractions(bookingService);
    }
}