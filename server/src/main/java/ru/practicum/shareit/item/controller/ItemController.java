package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.IncomingCommentDto;
import ru.practicum.shareit.comment.dto.OutgoingCommentDto;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.OutgoingItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

import static ru.practicum.shareit.http.HttpHeader.header;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
    private final ItemService itemService;

    @GetMapping
    public Collection<OutgoingItemDto> getAllOwnerItems(
            HttpServletRequest request,
            @RequestParam(value = "from") Integer firstElement,
            @RequestParam(value = "size") Integer size,
            @RequestHeader(header) Long ownerId) {
        log.info("Id-{} {} {}?{}", ownerId, request.getMethod(), request.getRequestURI(), request.getQueryString());
        return itemService.getAllOwnerItems(ownerId, PageRequest.of(firstElement / size, size, sortByStartDesc));
    }

    @GetMapping("/{itemId}")
    public OutgoingItemDto getItemById(
            HttpServletRequest request,
            @RequestHeader(header) Long ownerId,
            @PathVariable Long itemId) {
        log.info("Id-{} {} {}", ownerId, request.getMethod(), request.getRequestURI());
        return itemService.getItemDtoById(ownerId, itemId);
    }

    @GetMapping("/search")
    public Collection<OutgoingItemDto> getItemsBySearch(
            HttpServletRequest request,
            @RequestParam(value = "from") Integer firstElement,
            @RequestParam(value = "size") Integer size,
            @RequestHeader(header) Long userId,
            @RequestParam(value = "text") String text) {
        log.info("Id-{} {} {}?{}", userId, request.getMethod(), request.getRequestURI(), request.getQueryString());
        return itemService.getItemsBySearch(userId, text, PageRequest.of(firstElement / size, size));
    }

    @PostMapping
    public OutgoingItemDto postItem(
            HttpServletRequest request,
            @RequestHeader(header) Long ownerId,
            @RequestBody IncomingItemDto incomingItemDto) {
        log.info("Id-{} {} {} {}", ownerId, request.getMethod(), request.getRequestURI(), incomingItemDto);
        return itemService.postItem(ownerId, incomingItemDto);
    }

    @PostMapping("/{itemId}/comment")
    public OutgoingCommentDto postComment(
            HttpServletRequest request,
            @RequestHeader(header) Long authorId,
            @PathVariable Long itemId,
            @RequestBody IncomingCommentDto incomingCommentDto) {
        log.info("Id-{} {} {} {}", authorId, request.getMethod(), request.getRequestURI(), incomingCommentDto);
        return itemService.postComment(authorId, itemId, incomingCommentDto);
    }

    @PatchMapping("/{itemId}")
    public OutgoingItemDto patchItemById(
            HttpServletRequest request,
            @RequestHeader(header) Long ownerId,
            @PathVariable Long itemId,
            @RequestBody IncomingItemDto incomingItemDto) {
        log.info("Id-{} {} {} {}", ownerId, request.getMethod(), request.getRequestURI(), incomingItemDto);
        return itemService.patchItemById(ownerId, itemId, incomingItemDto);
    }

    @DeleteMapping("/{itemId}")
    public OutgoingItemDto deleteItemById(
            HttpServletRequest request,
            @RequestHeader(header) Long ownerId,
            @PathVariable Long itemId) {
        log.info("Id-{} {} {}", ownerId, request.getMethod(), request.getRequestURI());
        return itemService.deleteItemById(ownerId, itemId);
    }

}
