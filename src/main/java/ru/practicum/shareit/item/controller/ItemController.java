package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.ValidationMarker;
import ru.practicum.shareit.item.dto.IncomingCommentDto;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.OutgoingCommentDto;
import ru.practicum.shareit.item.dto.OutgoingItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Positive;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor
public class ItemController {

    private final String header = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    public Collection<OutgoingItemDto> getAllOwnerItems(@RequestHeader(header) @Positive Long ownerId) {
        log.info("\nGET /items\n{} {}\n", header, ownerId);
        return itemService.getAllOwnerItems(ownerId);
    }

    @GetMapping("/{itemId}")
    public OutgoingItemDto getItemById(@RequestHeader(header) @Positive Long ownerId,
                                       @PathVariable @Positive Long itemId) {
        log.info("\nGET /items/{}\n{} {}\n", itemId, header, ownerId);
        return itemService.getItemDtoById(ownerId, itemId);
    }

    @GetMapping("/search")
    public Collection<OutgoingItemDto> getItemsBySearch(@RequestHeader(header) @Positive Long userId,
                                                        @RequestParam String text) {
        log.info("\nGET /items/search/text={}\n{} {}\n", text, header, userId);
        return itemService.getItemsBySearch(userId, text);
    }

    @PostMapping
    public OutgoingItemDto postItem(
            @RequestHeader(header) @Positive Long ownerId,
            @RequestBody @Validated(ValidationMarker.OnCreate.class) IncomingItemDto incomingItemDto) {
        log.info("\nPOST /items\n{} {}\n{}\n", header, ownerId, incomingItemDto);
        return itemService.postItem(ownerId, incomingItemDto);
    }

    @PostMapping("/{itemId}/comment")
    public OutgoingCommentDto postComment(
            @RequestHeader(header) @Positive Long authorId,
            @PathVariable @Positive Long itemId,
            @RequestBody @Validated(ValidationMarker.OnCreate.class) IncomingCommentDto incomingCommentDto) {
        log.info("\nPOST /items/{}/comment\n{} {}\n{}\n", itemId, header, authorId, incomingCommentDto);
        return itemService.postComment(authorId, itemId, incomingCommentDto);
    }

    @PatchMapping("/{itemId}")
    public OutgoingItemDto patchItemById(
            @RequestHeader(header) @Positive Long ownerId,
            @PathVariable @Positive Long itemId,
            @RequestBody @Validated(ValidationMarker.OnUpdate.class) IncomingItemDto incomingItemDto) {
        log.info("\nPATCH /items/{}\n{} {}\n{}\n", itemId, header, ownerId, incomingItemDto);
        return itemService.patchItemById(ownerId, itemId, incomingItemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItemById(@RequestHeader(header) @Positive Long ownerId,
                               @PathVariable @Positive Long itemId) {
        log.info("\nDELETE items/{}\n{} {}\n", itemId, header, ownerId);
        itemService.deleteItemById(ownerId, itemId);
    }

}
