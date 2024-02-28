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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    public Collection<OutgoingBookingDto> getAllUserBookings(Long bookerId,
                                                             BookingState bookingState,
                                                             Pageable pageable) {
        userRepository.checkUserById(bookerId);
        switch (bookingState) {
            case ALL:
                return bookingRepository.findByBookerId(bookerId, pageable);
            case CURRENT:
                return bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBefore(bookerId,
                        LocalDateTime.now(), LocalDateTime.now(), pageable);
            case PAST:
                return bookingRepository.findByBookerIdAndEndIsBefore(bookerId,
                        LocalDateTime.now(), pageable);
            case FUTURE:
                return bookingRepository.findByBookerIdAndStartIsAfter(bookerId,
                        LocalDateTime.now(), pageable);
            case WAITING:
                return bookingRepository.findByBookerIdAndStatusIs(bookerId,
                        BookingStatus.WAITING, pageable);
            case REJECTED:
                return bookingRepository.findByBookerIdAndStatusIs(bookerId,
                        BookingStatus.REJECTED, pageable);
            default:
                return List.of();
        }
    }

    @Override
    public Collection<OutgoingBookingDto> getAllOwnerItemBookings(Long ownerId,
                                                                  BookingState bookingState,
                                                                  Pageable pageable) {
        List<Item> ownerItemList = itemRepository.findByOwnerId(ownerId);
        if (ownerItemList.isEmpty()) {
            throw new NotFoundException("There's no items belong to user " + ownerId);
        }
        List<Long> ownerItemIdList = ownerItemList.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        switch (bookingState) {
            case ALL:
                return bookingRepository.findByItemIdIn(ownerItemIdList,
                        OutgoingBookingDto.class, pageable);
            case CURRENT:
                return bookingRepository.findByItemIdInAndStartIsBeforeAndEndIsAfter(ownerItemIdList,
                        LocalDateTime.now(), LocalDateTime.now(), pageable);
            case PAST:
                return bookingRepository.findByItemIdInAndEndIsBefore(ownerItemIdList,
                        LocalDateTime.now(), pageable);
            case FUTURE:
                return bookingRepository.findByItemIdInAndStartIsAfter(ownerItemIdList,
                        LocalDateTime.now(), pageable);
            case WAITING:
                return bookingRepository.findByItemIdInAndStatusIs(ownerItemIdList,
                        BookingStatus.WAITING, pageable);
            case REJECTED:
                return bookingRepository.findByItemIdInAndStatusIs(ownerItemIdList,
                        BookingStatus.REJECTED, pageable);
            default:
                return List.of();
        }
    }

    public OutgoingBookingDto getBookingById(Long userId, Long bookingId) {
        OutgoingBookingDto outgoingBookingDto = getBookingById(bookingId, OutgoingBookingDto.class);
        if (!outgoingBookingDto.getItem().getOwner().getId().equals(userId)
                && !outgoingBookingDto.getBooker().getId().equals(userId)) {
            throw new NotFoundException("Requesting booking is not created by user " + userId +
                    " and booking item don't belong to him!");
        }
        return outgoingBookingDto;
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
        Booking booking = bookingMapper.mapIncomingDtoToBooking(incomingBookingDto);
        booking.setBooker(booker);
        booking.setItem(item);
        return bookingMapper.mapBookingToOutgoingDto(bookingRepository.save(booking));
    }

    public OutgoingBookingDto patchBookingById(Long itemOwnerId, Long bookingId, Boolean approved) {
        Booking booking = getBookingById(bookingId, Booking.class);
        if (BookingStatus.WAITING != booking.getStatus()) {
            throw new NotAvailableItemException("Not allowed to change booking status " + booking.getStatus());
        }
        if (!booking.getItem().getOwner().getId().equals(itemOwnerId)) {
            throw new NotFoundException("Booking item don't belong to user with id " + itemOwnerId);
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingMapper.mapBookingToOutgoingDto(bookingRepository.save(booking));
    }

    private <T> T getBookingById(Long bookingId, Class<T> projectionClass) {
        return bookingRepository.findById(bookingId, projectionClass)
                .orElseThrow(() -> new NotFoundException("There's no booking with id " + bookingId));
    }

}
