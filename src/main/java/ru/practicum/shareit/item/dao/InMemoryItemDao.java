package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemDao implements ItemDao {

    private final Map<Long, Item> itemMap = new HashMap<>();

    private Long id = 0L;

    @Override
    public Collection<Item> getAllOwnerItems(Long ownerId) {
        return itemMap.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemMap.values().stream()
                .filter(item -> Objects.equals(item.getId(), itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("There's no item with id " + itemId));
    }

    @Override
    public Collection<Item> getItemsBySearch(Long ownerId, String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemMap.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Item postItem(Long ownerId, Item item) {
        item.setId(generateItemId());
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public Item patchItemById(Long ownerId, Long itemId, Item item) {
        Item updatingItem = getItemById(itemId);
        if (!Objects.equals(updatingItem.getOwner().getId(), ownerId)) {
            throw new NotFoundException("Item with id " + itemId + " doesn't belongs to user with id " + ownerId);
        }
        if (null != item.getName() && !item.getName().isBlank()) {
            updatingItem.setName(item.getName());
        }
        if (null != item.getDescription() && !item.getDescription().isBlank()) {
            updatingItem.setDescription(item.getDescription());
        }
        if (null != item.getAvailable()) {
            updatingItem.setAvailable(item.getAvailable());
        }
        return updatingItem;
    }

    @Override
    public void deleteItemById(Long ownerId, Long itemId) {
        itemMap.remove(getItemById(itemId).getId());
    }

    @Override
    public void deleteAllOwnerItems(Long ownerId) {
        getAllOwnerItems(ownerId).stream()
                .peek(item -> itemMap.remove(item.getId()));
    }

    private Long generateItemId() {
        return ++id;
    }
}
