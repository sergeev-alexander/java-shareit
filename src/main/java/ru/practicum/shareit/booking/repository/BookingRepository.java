package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.dto.LastNextBookingDto;
import ru.practicum.shareit.booking.dto.OutgoingBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<OutgoingBookingDto> findByBookerId(Long bookerId, Pageable pageable);

    <T> Optional<T> findById(Long bookingId, Class<T> projectionClass);

    List<OutgoingBookingDto> findByBookerIdAndEndIsAfterAndStartIsBefore(Long bookerId,
                                                                         LocalDateTime now,
                                                                         LocalDateTime sameNow,
                                                                         Pageable pageable);

    List<OutgoingBookingDto> findByBookerIdAndEndIsBefore(Long bookerId,
                                                          LocalDateTime now,
                                                          Pageable pageable);

    List<OutgoingBookingDto> findByBookerIdAndStartIsAfter(Long bookerId,
                                                           LocalDateTime now,
                                                           Pageable pageable);

    List<OutgoingBookingDto> findByBookerIdAndStatusIs(Long bookerId,
                                                       BookingStatus status,
                                                       Pageable pageable);

    <T> List<T> findByItemIdIn(Collection<Long> ownerItemsIdList,
                               Class<T> projectionClass,
                               Pageable pageable);

    List<OutgoingBookingDto> findByItemIdInAndStartIsBeforeAndEndIsAfter(List<Long> ownerItemIdList,
                                                                         LocalDateTime now,
                                                                         LocalDateTime sameNow,
                                                                         Pageable pageable);

    List<OutgoingBookingDto> findByItemIdInAndEndIsBefore(List<Long> ownerItemIdList,
                                                          LocalDateTime now,
                                                          Pageable pageable);

    List<OutgoingBookingDto> findByItemIdInAndStartIsAfter(List<Long> ownerItemIdList,
                                                           LocalDateTime now,
                                                           Pageable pageable);

    List<OutgoingBookingDto> findByItemIdInAndStatusIs(List<Long> ownerItemIdList,
                                                       BookingStatus status,
                                                       Pageable pageable);

    Optional<LastNextBookingDto> findFirstByItemIdAndStartIsAfterAndStatusIs(Long itemId,
                                                                             LocalDateTime now,
                                                                             BookingStatus bookingStatus,
                                                                             Sort sort);

    Optional<LastNextBookingDto> findFirstByItemIdAndStartIsBeforeAndStatusIs(Long itemId,
                                                                              LocalDateTime now,
                                                                              BookingStatus bookingStatus,
                                                                              Sort sort);

    Boolean existsByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(Long authorId,
                                                               Long itemId,
                                                               LocalDateTime now,
                                                               BookingStatus bookingStatus);

}
