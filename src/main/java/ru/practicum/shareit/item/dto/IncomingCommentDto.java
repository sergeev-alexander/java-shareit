package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.exeption.ValidationMarker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class IncomingCommentDto {

    @Null(groups = ValidationMarker.OnCreate.class, message = "Creating comment already has an id!")
    private Long id;

    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "Creating comment text field is blank!")
    @Size(groups = ValidationMarker.OnCreate.class, max = 128,
            message = "Creating comment text field is bigger than 128 characters!")
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IncomingCommentDto)) return false;
        IncomingCommentDto that = (IncomingCommentDto) o;
        return Objects.equals(getId(), that.getId())
                && Objects.equals(getText(), that.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText());
    }

    @Override
    public String toString() {
        return "IncomingCommentDto{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }

}
