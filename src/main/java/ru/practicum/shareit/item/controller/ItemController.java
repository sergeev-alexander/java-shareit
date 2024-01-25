package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImplementation;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserServiceImplementation;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final String header = "X-Sharer-User-Id";
    private final ItemServiceImplementation itemService;
    private final UserServiceImplementation userService;
    private final UserMapper userMapper;

    @GetMapping
    public Collection<ItemDto> getAllOwnerItems(@RequestHeader(header) @Positive Long ownerId) {
        userService.getUserById(ownerId);
        return itemService.getAllOwnerItems(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(header) @Positive Long ownerId,
                                    @PathVariable @Positive Long itemId) {
        userService.getUserById(ownerId);
        return itemService.getItemById(itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> getItemsBySearch(@RequestHeader(header) @Positive Long ownerId,
                                                @RequestParam String text) {
        userService.getUserById(ownerId);
        return itemService.getItemsBySearch(ownerId, text);
    }

    @PostMapping
    public ItemDto postItem(@RequestHeader(header) @Positive Long ownerId,
                            @RequestBody @Valid Item item) {
        item.setOwner(userMapper.mapDtoToUser(userService.getUserById(ownerId)));
        return itemService.postItem(ownerId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItemById(@RequestHeader(header) @Positive Long ownerId,
                                 @PathVariable @Positive Long itemId,
                                 @RequestBody Item item) {
        item.setOwner(userMapper.mapDtoToUser(userService.getUserById(ownerId)));
        return itemService.patchItemById(ownerId, itemId, item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItemById(@RequestHeader(header) @Positive Long ownerId,
                               @PathVariable @Positive Long itemId) {
        userService.getUserById(ownerId);
        itemService.deleteItemById(ownerId, itemId);
    }

}
