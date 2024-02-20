package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.LastNextBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exeption.NotAvailableItemException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public Collection<OutgoingItemDto> getAllOwnerItems(Long ownerId) {
        userRepository.checkUserById(ownerId);
        List<OutgoingItemDto> outgoingItemDtoList = itemRepository.findByOwnerId(ownerId)
                .stream()
                .map(itemMapper::mapItemToOutgoingDto)
                .collect(Collectors.toList());
        List<Long> itemIdList = outgoingItemDtoList
                .stream()
                .map(OutgoingItemDto::getId)
                .collect(Collectors.toList());
        List<Comment> commentList = commentRepository.findByItemIdIn(itemIdList);
        List<Booking> bookingList = bookingRepository
                .findByItemIdIn(itemIdList, Sort.by(Sort.Direction.ASC, "start"), Booking.class);
        return outgoingItemDtoList
                .stream()
                .peek(outgoingItemDto -> outgoingItemDto.setComments(commentList
                        .stream()
                        .filter(comment -> comment.getItem().getId().equals(outgoingItemDto.getId()))
                        .map(commentMapper::mapCommentToOutgoingDto)
                        .collect(Collectors.toList())))
                .peek(outgoingItemDto -> outgoingItemDto.setLastBooking(bookingList
                        .stream()
                        .filter(booking -> booking.getItem().getId().equals(outgoingItemDto.getId())
                                && booking.getStart().isBefore(LocalDateTime.now()))
                        .map(bookingMapper::mapBookingToLastNextDto)
                        .reduce((booking1, booking2) -> booking2)
                        .orElse(null)))
                .peek(outgoingItemDto -> outgoingItemDto.setNextBooking(bookingList
                        .stream()
                        .filter(booking -> booking.getItem().getId().equals(outgoingItemDto.getId())
                                && booking.getStart().isAfter(LocalDateTime.now()))
                        .map(bookingMapper::mapBookingToLastNextDto)
                        .findFirst()
                        .orElse(null)))
                .collect(Collectors.toList());
    }

    @Override
    public OutgoingItemDto getItemDtoById(Long userId, Long itemId) {
        userRepository.checkUserById(userId);
        Item item = itemRepository.getItemById(itemId);
        OutgoingItemDto outgoingItemDto = itemMapper.mapItemToOutgoingDto(item);
        if (item.getOwner().getId().equals(userId)) {
            outgoingItemDto.setLastBooking(getLastBookingByItemId(itemId));
            outgoingItemDto.setNextBooking(getNextBookingByItemId(itemId));
        }
        outgoingItemDto.setComments(getCommentsByItemId(itemId));
        return outgoingItemDto;
    }

    @Override
    public Collection<OutgoingItemDto> getItemsBySearch(Long userId, String text) {
        userRepository.checkUserById(userId);
        if (text.isBlank()) {
            return List.of();
        }
        List<Item> itemList =
                itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                        text, text);
        List<Comment> commentList = commentRepository.findByItemIdIn(itemList
                .stream()
                .map(Item::getId)
                .collect(Collectors.toList()));
        return itemList
                .stream()
                .map(itemMapper::mapItemToOutgoingDto)
                .peek(outgoingItemDto -> outgoingItemDto.setComments(commentList
                        .stream()
                        .filter(comment -> comment.getItem().getId().equals(outgoingItemDto.getId()))
                        .map(commentMapper::mapCommentToOutgoingDto)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public OutgoingItemDto postItem(Long ownerId, IncomingItemDto incomingItemDto) {
        Item item = itemMapper.mapIncomingDtoToItem(incomingItemDto);
        item.setOwner(userRepository.getUserById(ownerId));
        return itemMapper.mapItemToOutgoingDto(itemRepository.save(item));
    }

    @Override
    public OutgoingCommentDto postComment(Long authorId, Long itemId, IncomingCommentDto incomingCommentDto) {
        bookingRepository.findByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(authorId,
                        itemId, LocalDateTime.now(), BookingStatus.APPROVED)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotAvailableItemException("Author with id " + authorId
                        + " has no rights to leve a comment to item with id " + itemId + "!"));
        Comment comment = commentMapper.mapIncommingDtoToComment(incomingCommentDto);
        comment.setAuthor(userRepository.getUserById(authorId));
        comment.setItem(itemRepository.getItemById(itemId));
        return commentMapper.mapCommentToOutgoingDto(commentRepository.save(comment));
    }

    @Override
    public OutgoingItemDto patchItemById(Long ownerId, Long itemId, IncomingItemDto incomingItemDto) {
        Item item = itemRepository.getItemById(itemId);
        if (!Objects.equals(item.getOwner().getId(), ownerId)) {
            throw new NotFoundException("The updating item don't belong to user with id " + ownerId + "!");
        }
        if (null != incomingItemDto.getName() && !incomingItemDto.getName().isBlank()) {
            item.setName(incomingItemDto.getName());
        }
        if (null != incomingItemDto.getDescription() && !incomingItemDto.getDescription().isBlank()) {
            item.setDescription(incomingItemDto.getDescription());
        }
        if (null != incomingItemDto.getAvailable()) {
            item.setAvailable(incomingItemDto.getAvailable());
        }
        return itemMapper.mapItemToOutgoingDto(itemRepository.save(item));
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

    private LastNextBookingDto getNextBookingByItemId(Long itemId) {
        return bookingRepository.findFirstByItemIdAndStartIsAfterAndStatusIs(itemId, LocalDateTime.now(),
                BookingStatus.APPROVED, Sort.by(Sort.Direction.ASC, "start")).orElse(null);
    }

    private LastNextBookingDto getLastBookingByItemId(Long itemId) {
        return bookingRepository.findFirstByItemIdAndStartIsBeforeAndStatusIs(itemId, LocalDateTime.now(),
                BookingStatus.APPROVED, Sort.by(Sort.Direction.DESC, "end")).orElse(null);
    }

    private List<OutgoingCommentDto> getCommentsByItemId(Long itemId) {
        return commentRepository.findByItemId(itemId).stream()
                .map(commentMapper::mapCommentToOutgoingDto)
                .collect(Collectors.toList());
    }

}
