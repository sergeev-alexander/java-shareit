package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.ValidationMarker;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final String header = "X-Sharer-User-Id";
    private final ItemServiceImpl itemService;

    @GetMapping
    public Collection<ItemDto> getAllOwnerItems(@RequestHeader(header) @Positive Long ownerId) {
        return itemService.getAllOwnerItems(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(header) @Positive Long ownerId,
                               @PathVariable @Positive Long itemId) {
        return itemService.getItemById(ownerId, itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> getItemsBySearch(@RequestHeader(header) @Positive Long ownerId,
                                                @RequestParam String text) {
        return itemService.getItemsBySearch(ownerId, text);
    }

    @PostMapping
    public ItemDto postItem(@RequestHeader(header) @Positive Long ownerId,
                            @RequestBody @Validated(ValidationMarker.OnCreate.class) ItemDto itemDto) {
        return itemService.postItem(ownerId, itemDto);
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
