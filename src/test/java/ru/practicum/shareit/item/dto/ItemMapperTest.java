package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemMapperTest {

    @Test
    void mapItemToOutgoingDto() {
        Item item = new Item(
                1L,
                "Item name",
                "Item description",
                true,
                new Request(
                        1L,
                        null,
                        null,
                        null),
                null);
        OutgoingItemDto result = ItemMapper.mapItemToOutgoingDto(item);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertEquals(item.getRequest().getId(), result.getRequestId());
    }

    @Test
    void mapIncomingDtoToItem() {
        IncomingItemDto incomingItemDto = new IncomingItemDto(
                "Item name",
                "Item description",
                true,
                1L);
        Item result = ItemMapper.mapIncomingDtoToItem(incomingItemDto);
        assertEquals(incomingItemDto.getName(), result.getName());
        assertEquals(incomingItemDto.getDescription(), result.getDescription());
        assertEquals(incomingItemDto.getAvailable(), result.getAvailable());
    }

}
