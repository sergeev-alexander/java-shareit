package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exeption.NotAvailableItemException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Override
    public Collection<ItemDto> getAllOwnerItems(Long ownerId) {
        return itemRepository.findByOwnerId(ownerId)
                .stream()
                .map(itemMapper::mapItemToDto)
                .peek(itemDto -> itemDto.setLastBooking(getLastBookingByItemId(itemDto.getId())))
                .peek(itemDto -> itemDto.setNextBooking(getNextBookingByItemId(itemDto.getId())))
                .peek(itemDto -> itemDto.setComments(getCommentsByItemId(itemDto.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemDtoById(Long userId, Long itemId) {
        getUserById(userId);
        Item item = getItemById(itemId);
        ItemDto itemDto = itemMapper.mapItemToDto(item);
        if (item.getOwner().getId().equals(userId)) {
            itemDto.setLastBooking(getLastBookingByItemId(itemId));
            itemDto.setNextBooking(getNextBookingByItemId(itemId));
        }
        itemDto.setComments(getCommentsByItemId(itemId));
        return itemDto;
    }

    @Override
    public Collection<ItemDto> getItemsBySearch(Long userId, String text) {
        getUserById(userId);
        if (text.isBlank()) {
            return List.of();
        }
        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                        text, text)
                .stream()
                .map(itemMapper::mapItemToDto)
                .peek(itemDto -> itemDto.setComments(getCommentsByItemId(itemDto.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto postItem(Long ownerId, ItemDto itemDto) {
        Item item = itemMapper.mapDtoToItem(itemDto);
        item.setOwner(getUserById(ownerId));
        return itemMapper.mapItemToDto(itemRepository.save(item));
    }

    @Override
    public CommentDto postComment(Long authorId, Long itemId, CommentDto commentDto) {
        bookingRepository.findByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(authorId,
                        itemId, LocalDateTime.now(), BookingStatus.APPROVED)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotAvailableItemException("Author with id " + authorId
                        + " has no rights to leve a comment to item with id " + itemId + "!"));
        Comment comment = commentMapper.mapDtoToComment(commentDto);
        comment.setAuthor(getUserById(authorId));
        comment.setItem(getItemById(itemId));
        return commentMapper.mapCommentToDto(commentRepository.save(comment));
    }

    @Override
    public ItemDto patchItemById(Long ownerId, Long itemId, ItemDto itemDto) {
        Item item = getItemById(itemId);
        if (!Objects.equals(item.getOwner().getId(), ownerId)) {
            throw new NotFoundException("The updating item don't belong to user with id " + ownerId + "!");
        }
        if (null != itemDto.getName()) {
            item.setName(itemDto.getName());
        }
        if (null != itemDto.getDescription()) {
            item.setDescription(itemDto.getDescription());
        }
        if (null != itemDto.getAvailable()) {
            item.setAvailable(itemDto.getAvailable());
        }
        return itemMapper.mapItemToDto(itemRepository.save(item));
    }

    @Override
    public void deleteItemById(Long ownerId, Long itemId) {
        itemRepository.delete(itemRepository.findByIdAndOwnerId(itemId, ownerId)
                .orElseThrow(() -> new NotFoundException("Owner with id " + ownerId
                        + " has no item with id " + itemId + "!")));
    }

    @Override
    public void deleteAllOwnerItems(Long ownerId) {
        itemRepository.deleteByOwner(ownerId);
    }

    private Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("There's no item with id " + itemId + "!"));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("There's no user with id " + userId + "!"));
    }

    private ItemDto.BookingDto getNextBookingByItemId(Long itemId) {
        return bookingRepository.findByItemIdAndStartIsAfterAndStatusIs(itemId, LocalDateTime.now(),
                        BookingStatus.APPROVED, Sort.by(Sort.Direction.ASC, "start"))
                .stream()
                .findFirst()
                .orElse(null);
    }

    private ItemDto.BookingDto getLastBookingByItemId(Long itemId) {
        return bookingRepository.findByItemIdAndStartIsBeforeAndStatusIs(itemId, LocalDateTime.now(),
                        BookingStatus.APPROVED, Sort.by(Sort.Direction.DESC, "end"))
                .stream()
                .findFirst()
                .orElse(null);
    }

    private List<CommentDto> getCommentsByItemId(Long itemId) {
        return commentRepository.findByItemId(itemId).stream()
                .map(commentMapper::mapCommentToDto)
                .collect(Collectors.toList());
    }

}
