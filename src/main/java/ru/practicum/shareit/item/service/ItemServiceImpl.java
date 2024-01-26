package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl {

    private final ItemDao itemDao;
    private final UserDao userDao;
    private final ItemMapper itemMapper;

    public Collection<ItemDto> getAllOwnerItems(Long ownerId) {
        userDao.getUserById(ownerId);
        return itemDao.getAllOwnerItems(ownerId).stream()
                .map(itemMapper::mapItemToDto)
                .collect(Collectors.toList());
    }

    public ItemDto getItemById(Long ownerId, Long itemId) {
        userDao.getUserById(ownerId);
        return itemMapper.mapItemToDto(itemDao.getItemById(itemId));
    }

    public Collection<ItemDto> getItemsBySearch(Long ownerId, String text) {
        userDao.getUserById(ownerId);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemDao.getItemsBySearch(ownerId, text).stream()
                .map(itemMapper::mapItemToDto)
                .collect(Collectors.toList());
    }

    public ItemDto postItem(Long ownerId, ItemDto itemDto) {
        Item item = itemMapper.mapDtoToItem(itemDto);
        item.setOwner(userDao.getUserById(ownerId));
        return itemMapper.mapItemToDto(itemDao.postItem(ownerId, item));
    }

    public ItemDto patchItemById(Long ownerId, Long itemId, ItemDto itemDto) {
        Item item = itemMapper.mapDtoToItem(itemDto);
        item.setOwner(userDao.getUserById(ownerId));
        return itemMapper.mapItemToDto(itemDao.patchItemById(ownerId, itemId, item));
    }

    public void deleteItemById(Long ownerId, Long itemId) {
        userDao.getUserById(ownerId);
        itemDao.deleteItemById(ownerId, itemId);
    }

    public void deleteAllOwnerItems(Long ownerId) {
        userDao.getUserById(ownerId);
        itemDao.deleteAllOwnerItems(ownerId);
    }

}
