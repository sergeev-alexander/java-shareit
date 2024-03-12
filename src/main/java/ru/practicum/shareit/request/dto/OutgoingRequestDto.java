package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.dto.OutgoingItemDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutgoingRequestDto {

    private Long id;

    private String description;

    private LocalDateTime created;

    private Long requesterId;

    private List<OutgoingItemDto> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OutgoingRequestDto)) return false;
        OutgoingRequestDto that = (OutgoingRequestDto) o;
        return Objects.equals(getId(), that.getId())
                && Objects.equals(getDescription(), that.getDescription())
                && Objects.equals(getCreated(), that.getCreated())
                && Objects.equals(getRequesterId(), that.getRequesterId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getCreated(), getRequesterId());
    }

    @Override
    public String toString() {
        return "OutgoingRequestDto{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", created=" + created +
                ", requesterId=" + requesterId +
                ", items=" + items +
                '}';
    }

}
