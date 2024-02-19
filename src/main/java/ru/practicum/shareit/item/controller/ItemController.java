package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.ValidationMarker;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
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
    public Collection<ItemDto> getAllOwnerItems(@RequestHeader(header) @Positive Long ownerId) {
        log.info("\nGET /items\n{} {}\n", header, ownerId);
        return itemService.getAllOwnerItems(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(header) @Positive Long ownerId,
                               @PathVariable @Positive Long itemId) {
        log.info("\nGET /items/{}\n{} {}\n",itemId ,header, ownerId);
        return itemService.getItemDtoById(ownerId, itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> getItemsBySearch(@RequestHeader(header) @Positive Long userId,
                                                @RequestParam String text) {
        log.info("\nGET /items/search/text={}\n{} {}\n", text, header, userId);
        return itemService.getItemsBySearch(userId, text);
    }

    @PostMapping
    public ItemDto postItem(@RequestHeader(header) @Positive Long ownerId,
                            @RequestBody @Validated(ValidationMarker.OnCreate.class) ItemDto itemDto) {
        log.info("\nPOST /items\n{} {}\n{}\n", header, ownerId, itemDto);
        return itemService.postItem(ownerId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@RequestHeader(header) @Positive Long authorId,
                                  @PathVariable @Positive Long itemId,
                                  @RequestBody @Validated(ValidationMarker.OnCreate.class) CommentDto commentDto) {
        log.info("\nPOST /items/{}/comment\n{} {}\n{}\n", itemId, header, authorId, commentDto);
        return itemService.postComment(authorId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItemById(@RequestHeader(header) @Positive Long ownerId,
                                 @PathVariable @Positive Long itemId,
                                 @RequestBody ItemDto itemDto) {
        log.info("\nPATCH /items/{}\n{} {}\n{}\n", itemId, header, ownerId, itemDto);
        return itemService.patchItemById(ownerId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItemById(@RequestHeader(header) @Positive Long ownerId,
                               @PathVariable @Positive Long itemId) {
        log.info("\nDELETE items/{}\n{} {}\n", itemId, header, ownerId);
        itemService.deleteItemById(ownerId, itemId);
    }

}
