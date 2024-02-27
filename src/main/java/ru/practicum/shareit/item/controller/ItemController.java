package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.ValidationMarker;
import ru.practicum.shareit.item.dto.IncomingCommentDto;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.OutgoingCommentDto;
import ru.practicum.shareit.item.dto.OutgoingItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor
public class ItemController {

    private final String header = "X-Sharer-User-Id";
    private final Sort sortByStartAsc = Sort.by(Sort.Direction.ASC, "start");

    private final ItemService itemService;

    @GetMapping
    public Collection<OutgoingItemDto> getAllOwnerItems(
            HttpServletRequest request,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(value = "size", defaultValue = "20") @Min(1) @Max(100) Integer size,
            @RequestHeader(header) @Positive Long ownerId) {
        log.info("Id-{} {} {}?{}", ownerId, request.getMethod(), request.getRequestURI(), request.getQueryString());
        return itemService.getAllOwnerItems(ownerId, PageRequest.of(offset, size, sortByStartAsc));
    }

    @GetMapping("/{itemId}")
    public OutgoingItemDto getItemById(
            HttpServletRequest request,
            @RequestHeader(header) @Positive Long ownerId,
            @PathVariable @Positive Long itemId) {
        log.info("Id-{} {} {}", ownerId, request.getMethod(), request.getRequestURI());
        return itemService.getItemDtoById(ownerId, itemId);
    }

    @GetMapping("/search")
    public Collection<OutgoingItemDto> getItemsBySearch(
            HttpServletRequest request,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(value = "size", defaultValue = "20") @Min(1) @Max(100) Integer size,
            @RequestHeader(header) @Positive Long userId,
            @RequestParam String text) {
        log.info("Id-{} {} {}?{}", userId, request.getMethod(), request.getRequestURI(), request.getQueryString());
        return itemService.getItemsBySearch(userId, text, PageRequest.of(offset, size));
    }

    @PostMapping
    public OutgoingItemDto postItem(
            HttpServletRequest request,
            @RequestHeader(header) @Positive Long ownerId,
            @RequestBody @Validated(ValidationMarker.OnCreate.class) IncomingItemDto incomingItemDto) {
        log.info("Id-{} {} {} {}", ownerId, request.getMethod(), request.getRequestURI(), incomingItemDto);
        return itemService.postItem(ownerId, incomingItemDto);
    }

    @PostMapping("/{itemId}/comment")
    public OutgoingCommentDto postComment(
            HttpServletRequest request,
            @RequestHeader(header) @Positive Long authorId,
            @PathVariable @Positive Long itemId,
            @RequestBody @Validated(ValidationMarker.OnCreate.class) IncomingCommentDto incomingCommentDto) {
        log.info("Id-{} {} {} {}", authorId, request.getMethod(), request.getRequestURI(), incomingCommentDto);
        return itemService.postComment(authorId, itemId, incomingCommentDto);
    }

    @PatchMapping("/{itemId}")
    public OutgoingItemDto patchItemById(
            HttpServletRequest request,
            @RequestHeader(header) @Positive Long ownerId,
            @PathVariable @Positive Long itemId,
            @RequestBody @Validated(ValidationMarker.OnUpdate.class) IncomingItemDto incomingItemDto) {
        log.info("Id-{} {} {} {}", ownerId, request.getMethod(), request.getRequestURI(), incomingItemDto);
        return itemService.patchItemById(ownerId, itemId, incomingItemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItemById(
            HttpServletRequest request,
            @RequestHeader(header) @Positive Long ownerId,
            @PathVariable @Positive Long itemId) {
        log.info("Id-{} {} {}", ownerId, request.getMethod(), request.getRequestURI());
        itemService.deleteItemById(ownerId, itemId);
    }

}
