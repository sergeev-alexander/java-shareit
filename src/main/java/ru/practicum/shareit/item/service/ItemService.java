package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.IncomingCommentDto;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.OutgoingCommentDto;
import ru.practicum.shareit.item.dto.OutgoingItemDto;

import java.util.Collection;

public interface ItemService {

    Collection<OutgoingItemDto> getAllOwnerItems(Long ownerId);

    OutgoingItemDto getItemDtoById(Long userId, Long itemId);

    Collection<OutgoingItemDto> getItemsBySearch(Long userId, String text);

    OutgoingItemDto postItem(Long ownerId, IncomingItemDto incomingItemDto);

    OutgoingCommentDto postComment(Long authorId, Long itemId, IncomingCommentDto incomingCommentDto);

    OutgoingItemDto patchItemById(Long ownerId, Long itemId, IncomingItemDto incomingItemDto);

    void deleteItemById(Long ownerId, Long itemId);

    void deleteAllOwnerItems(Long ownerId);

}
