package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDto> getAllOwnerItems(Long ownerId);

    ItemDto getItemById(Long userId, Long itemId);

    Collection<ItemDto> getItemsBySearch(Long ownerId, String text);

    ItemDto postItem(Long ownerId, ItemDto itemDto);

    ItemDto patchItemById(Long ownerId, Long itemId, ItemDto itemDto);

    void deleteItemById(Long ownerId, Long itemId);

    void deleteAllOwnerItems(Long ownerId);

}
