package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.dto.OutgoingBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exeption.NotAvailableItemException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<OutgoingBookingDto> getAllUserBookings(Long bookerId,
                                                       BookingState bookingState,
                                                       Pageable pageable) {
        userRepository.checkUserById(bookerId);
        LocalDateTime now = LocalDateTime.now();
        switch (bookingState) {
            case ALL:
                return bookingRepository.findByBookerId(bookerId, pageable)
                        .stream()
                        .map(BookingMapper::mapBookingToOutgoingDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBefore(bookerId,
                                now, now, pageable)
                        .stream()
                        .map(BookingMapper::mapBookingToOutgoingDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findByBookerIdAndEndIsBefore(bookerId,
                                now, pageable)
                        .stream()
                        .map(BookingMapper::mapBookingToOutgoingDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findByBookerIdAndStartIsAfter(bookerId,
                                now, pageable)
                        .stream()
                        .map(BookingMapper::mapBookingToOutgoingDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findByBookerIdAndStatusIs(bookerId,
                                BookingStatus.WAITING, pageable)
                        .stream()
                        .map(BookingMapper::mapBookingToOutgoingDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findByBookerIdAndStatusIs(bookerId,
                                BookingStatus.REJECTED, pageable)
                        .stream()
                        .map(BookingMapper::mapBookingToOutgoingDto)
                        .collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public List<OutgoingBookingDto> getAllOwnerItemBookings(Long ownerId,
                                                            BookingState bookingState,
                                                            Pageable pageable) {
        List<Item> ownerItemList = itemRepository.findByOwnerId(ownerId);
        if (ownerItemList.isEmpty()) {
            throw new NotFoundException("There's no items belong to user " + ownerId);
        }
        LocalDateTime now = LocalDateTime.now();
        List<Long> ownerItemIdList = ownerItemList.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        switch (bookingState) {
            case ALL:
                return bookingRepository.findByItemIdIn(ownerItemIdList, pageable)
                        .stream()
                        .map(BookingMapper::mapBookingToOutgoingDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findByItemIdInAndStartIsBeforeAndEndIsAfter(ownerItemIdList,
                                now, now, pageable)
                        .stream()
                        .map(BookingMapper::mapBookingToOutgoingDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findByItemIdInAndEndIsBefore(ownerItemIdList,
                                now, pageable)
                        .stream()
                        .map(BookingMapper::mapBookingToOutgoingDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findByItemIdInAndStartIsAfter(ownerItemIdList,
                                now, pageable)
                        .stream()
                        .map(BookingMapper::mapBookingToOutgoingDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findByItemIdInAndStatusIs(ownerItemIdList,
                                BookingStatus.WAITING, pageable)
                        .stream()
                        .map(BookingMapper::mapBookingToOutgoingDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findByItemIdInAndStatusIs(ownerItemIdList,
                                BookingStatus.REJECTED, pageable)
                        .stream()
                        .map(BookingMapper::mapBookingToOutgoingDto)
                        .collect(Collectors.toList());
        }
        return List.of();
    }

    public OutgoingBookingDto getBookingById(Long userId, Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (!booking.getItem().getOwner().getId().equals(userId)
                && !booking.getBooker().getId().equals(userId)) {
            throw new NotFoundException("Requesting booking is not created by user " + userId +
                    " and booking item don't belong to him!");
        }
        return BookingMapper.mapBookingToOutgoingDto(booking);
    }

    @Override
    public OutgoingBookingDto postBooking(Long bookerId, IncomingBookingDto incomingBookingDto) {
        User booker = userRepository.getUserById(bookerId);
        Item item = itemRepository.getItemById(incomingBookingDto.getItemId());
        if (item.getOwner().getId().equals(bookerId))
            throw new NotFoundException("Booking item belongs to booker!");
        if (!item.getAvailable()) {
            throw new NotAvailableItemException("Booking item is not available!");
        }
        Booking booking = BookingMapper.mapIncomingDtoToBooking(incomingBookingDto);
        booking.setBooker(booker);
        booking.setItem(item);
        return BookingMapper.mapBookingToOutgoingDto(bookingRepository.save(booking));
    }

    @Override
    public OutgoingBookingDto patchBookingById(Long itemOwnerId, Long bookingId, Boolean approved) {
        Booking booking = getBookingById(bookingId);
        if (BookingStatus.WAITING != booking.getStatus()) {
            throw new NotAvailableItemException("Not allowed to change booking status " + booking.getStatus());
        }
        if (!booking.getItem().getOwner().getId().equals(itemOwnerId)) {
            throw new NotFoundException("Booking item don't belong to user with id " + itemOwnerId);
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.mapBookingToOutgoingDto(bookingRepository.save(booking));
    }

    protected Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("There's no booking with id " + bookingId));
    }

}
