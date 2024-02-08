package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemDao {

    Collection<Item> getAllOwnerItems(Long ownerId);

    Item getItemById(Long itemId);

    Collection<Item> getItemsBySearch(Long ownerId, String text);

    Item postItem(Long ownerId, Item item);

    Item patchItemById(Long ownerId, Long itemId, Item item);

    void deleteItemById(Long ownerId, Long itemId);

    void deleteAllOwnerItems(Long ownerId);

}
