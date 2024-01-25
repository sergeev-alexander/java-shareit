package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDto> getAllOwnerItems(Long ownerId);

    ItemDto getItemById(Long itemId);

    Collection<ItemDto> getItemsBySearch(Long ownerId, String text);

    ItemDto postItem(Long ownerId, Item item);

    ItemDto patchItemById(Long ownerId, Long itemId, Item item);

    void deleteItemById(Long ownerId, Long itemId);

    void deleteAllOwnerItems(Long ownerId);

}
