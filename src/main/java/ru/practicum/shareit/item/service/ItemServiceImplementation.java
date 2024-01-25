package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImplementation {

    private final ItemDao itemDao;
    private final ItemMapper itemMapper;

    public Collection<ItemDto> getAllOwnerItems(Long ownerId) {
        return itemDao.getAllOwnerItems(ownerId).stream()
                .map(itemMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public ItemDto getItemById(Long itemId) {
        return itemMapper.mapToDto(itemDao.getItemById(itemId));
    }

    public Collection<ItemDto> getItemsBySearch(Long ownerId, String text) {
        return itemDao.getItemsBySearch(ownerId, text).stream()
                .map(itemMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public ItemDto postItem(Long ownerId, Item item) {
        return itemMapper.mapToDto(itemDao.postItem(ownerId, item));
    }

    public ItemDto patchItemById(Long ownerId, Long itemId, Item item) {
        return itemMapper.mapToDto(itemDao.patchItemById(ownerId, itemId, item));
    }

    public void deleteItemById(Long ownerId, Long itemId) {
        itemDao.deleteItemById(ownerId, itemId);
    }

    public void deleteAllOwnerItems(Long ownerId) {
        itemDao.deleteAllOwnerItems(ownerId);
    }

}
