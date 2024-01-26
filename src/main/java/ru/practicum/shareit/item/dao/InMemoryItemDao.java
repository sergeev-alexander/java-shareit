package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemDao implements ItemDao {

    private final Map<Long, Item> itemMap = new HashMap<>();
    private final Map<Long, List<Item>> ownerItemMap = new LinkedHashMap<>();

    private Long id = 0L;

    @Override
    public Collection<Item> getAllOwnerItems(Long ownerId) {
        return ownerItemMap.computeIfAbsent(ownerId, k -> new ArrayList<>());
    }

    @Override
    public Item getItemById(Long itemId) {
        Item item = itemMap.get(itemId);
        if (null == item) {
            throw new NotFoundException("There's no item with id " + itemId);
        }
        return item;
    }

    @Override
    public Collection<Item> getItemsBySearch(Long ownerId, String text) {
        final String searchTextLowerCase = text.toLowerCase();
        return itemMap.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(searchTextLowerCase)
                        || item.getDescription().toLowerCase().contains(searchTextLowerCase))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Item postItem(Long ownerId, Item item) {
        item.setId(generateItemId());
        itemMap.put(item.getId(), item);
        ownerItemMap.computeIfAbsent(ownerId, k -> new ArrayList<>()).add(item);
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
        Item item = getItemById(itemId);
        ownerItemMap.get(ownerId).remove(item);
        itemMap.remove(item.getId());
    }

    @Override
    public void deleteAllOwnerItems(Long ownerId) {
        ownerItemMap.remove(ownerId);
        getAllOwnerItems(ownerId).stream()
                .peek(item -> itemMap.remove(item.getId()));
    }

    private Long generateItemId() {
        return ++id;
    }

}
