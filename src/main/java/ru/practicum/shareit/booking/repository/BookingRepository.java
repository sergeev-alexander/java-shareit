package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.dto.OutgoingBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    <T> List<T> findByBookerId(Long bookerId,
                               Sort sort,
                               Class<T> projectionClass);

    <T> Optional<T> findById(Long bookingId, Class<T> projectionClass);

    List<OutgoingBookingDto> findByBookerIdAndEndIsAfterAndStartIsBefore(Long bookerId,
                                                                         LocalDateTime now,
                                                                         LocalDateTime sameNow,
                                                                         Sort sort);

    List<OutgoingBookingDto> findByBookerIdAndEndIsBefore(Long bookerId,
                                                          LocalDateTime now,
                                                          Sort sort);

    List<OutgoingBookingDto> findByBookerIdAndStartIsAfter(Long bookerId,
                                                           LocalDateTime now,
                                                           Sort sort);

    List<OutgoingBookingDto> findByBookerIdAndStatusIs(Long bookerId,
                                                       BookingStatus status,
                                                       Sort sort);

    List<OutgoingBookingDto> findByItemIdIn(Collection<Long> ownerItemsIdList,
                                            Sort sort);

    List<OutgoingBookingDto> findByItemIdInAndStartIsBeforeAndEndIsAfter(List<Long> ownerItemIdList,
                                                                         LocalDateTime now,
                                                                         LocalDateTime sameNow,
                                                                         Sort sort);

    List<OutgoingBookingDto> findByItemIdInAndEndIsBefore(List<Long> ownerItemIdList,
                                                          LocalDateTime now,
                                                          Sort sort);

    List<OutgoingBookingDto> findByItemIdInAndStartIsAfter(List<Long> ownerItemIdList,
                                                           LocalDateTime now,
                                                           Sort sort);

    List<OutgoingBookingDto> findByItemIdInAndStatusIs(List<Long> ownerItemIdList,
                                                       BookingStatus status,
                                                       Sort sort);

    List<ItemDto.BookingDto> findFirstByItemIdAndStartIsAfterAndStatusIs(Long itemId,
                                                                         LocalDateTime now,
                                                                         BookingStatus bookingStatus,
                                                                         Sort sort);

    List<ItemDto.BookingDto> findByItemIdAndStartIsBeforeAndStatusIs(Long itemId,
                                                                     LocalDateTime now,
                                                                     BookingStatus bookingStatus,
                                                                     Sort sort);

    List<Booking> findByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(Long authorId,
                                                                   Long itemId,
                                                                   LocalDateTime now,
                                                                   BookingStatus bookingStatus);

}
