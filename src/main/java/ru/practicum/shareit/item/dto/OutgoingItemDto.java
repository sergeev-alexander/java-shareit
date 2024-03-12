package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.LastNextBookingDto;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class OutgoingItemDto {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private LastNextBookingDto lastBooking;

    private LastNextBookingDto nextBooking;

    private List<OutgoingCommentDto> comments;

    private Long requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OutgoingItemDto)) return false;
        OutgoingItemDto that = (OutgoingItemDto) o;
        return Objects.equals(getId(), that.getId())
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getDescription(), that.getDescription())
                && Objects.equals(getAvailable(), that.getAvailable())
                && Objects.equals(getRequestId(), that.getRequestId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getAvailable(), getRequestId());
    }

    @Override
    public String toString() {
        return "OutgoingItemDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                ", lastBooking=" + lastBooking +
                ", nextBooking=" + nextBooking +
                ", comments=" + comments +
                ", requestId=" + requestId +
                '}';
    }

}
