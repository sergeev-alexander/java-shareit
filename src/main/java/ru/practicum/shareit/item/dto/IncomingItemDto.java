package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.exeption.ValidationMarker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class IncomingItemDto {

    @NotBlank(groups = ValidationMarker.OnCreate.class,
        message = "Creating item name field is blank!")
    @Size(groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class}, max = 128,
            message = "Creating item name field is bigger than 128 characters!")
    private String name;

    @NotBlank(groups = ValidationMarker.OnCreate.class,
            message = "Creating item description field is blank!")
    @Size(groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class}, max = 128,
            message = "Creating item description field is bigger than 128 characters!")
    private String description;

    @NotNull(groups = ValidationMarker.OnCreate.class,
            message = "Creating item available field is null!")
    private Boolean available;

    @Positive(groups = ValidationMarker.OnCreate.class,
            message = "Creating item requestId field must be positive!")
    private Long requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IncomingItemDto)) return false;
        IncomingItemDto that = (IncomingItemDto) o;
        return Objects.equals(getName(), that.getName())
                && Objects.equals(getDescription(), that.getDescription())
                && Objects.equals(getAvailable(), that.getAvailable())
                && Objects.equals(getRequestId(), that.getRequestId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getAvailable(), getRequestId());
    }

    @Override
    public String toString() {
        return "IncomingItemDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                ", requestId=" + requestId +
                '}';
    }

}
