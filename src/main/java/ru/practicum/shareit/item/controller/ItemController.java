package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.ValidationMarker;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor
public class ItemController {

    private final String header = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    public Collection<ItemDto> getAllOwnerItems(@RequestHeader(header) @Positive Long ownerId) {
        return itemService.getAllOwnerItems(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(header) @Positive Long ownerId,
                               @PathVariable @Positive Long itemId) {
        return itemService.getItemDtoById(ownerId, itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> getItemsBySearch(@RequestHeader(header) @Positive Long userId,
                                                @RequestParam String text) {
        return itemService.getItemsBySearch(userId, text);
    }

    @PostMapping
    public ItemDto postItem(@RequestHeader(header) @Positive Long ownerId,
                            @RequestBody @Validated(ValidationMarker.OnCreate.class) ItemDto itemDto) {
        return itemService.postItem(ownerId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@RequestHeader(header) @Positive Long authorId,
                                  @PathVariable @Positive Long itemId,
                                  @RequestBody @Validated(ValidationMarker.OnCreate.class) CommentDto commentDto) {
        return itemService.postComment(authorId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItemById(@RequestHeader(header) @Positive Long ownerId,
                                 @PathVariable @Positive Long itemId,
                                 @RequestBody ItemDto itemDto) {
        return itemService.patchItemById(ownerId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItemById(@RequestHeader(header) @Positive Long ownerId,
                               @PathVariable @Positive Long itemId) {
        itemService.deleteItemById(ownerId, itemId);
    }

}
