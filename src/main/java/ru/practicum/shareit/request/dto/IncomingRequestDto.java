package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.exeption.ValidationMarker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomingRequestDto {

    @Null(groups = ValidationMarker.OnCreate.class, message = "Creating request already has an id!")
    private Long id;

    @NotBlank(groups = ValidationMarker.OnCreate.class,
            message = "Creating request description field is blank!")
    @Size(groups = ValidationMarker.OnCreate.class, max = 128,
            message = "Creating request description field is bigger than 128 characters!")
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IncomingRequestDto)) return false;
        IncomingRequestDto that = (IncomingRequestDto) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription());
    }

    @Override
    public String toString() {
        return "IncomingRequestDto{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }

}
