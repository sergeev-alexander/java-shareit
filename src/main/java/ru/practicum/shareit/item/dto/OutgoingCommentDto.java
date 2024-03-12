package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class OutgoingCommentDto {

    private Long id;

    private String text;

    private String authorName;

    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OutgoingCommentDto)) return false;
        OutgoingCommentDto that = (OutgoingCommentDto) o;
        return Objects.equals(getId(), that.getId())
                && Objects.equals(getText(), that.getText())
                && Objects.equals(getAuthorName(), that.getAuthorName())
                && Objects.equals(getCreated(), that.getCreated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText(), getAuthorName(), getCreated());
    }

}
