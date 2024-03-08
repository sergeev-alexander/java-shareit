package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    public static OutgoingCommentDto mapCommentToOutgoingDto(Comment comment) {
        return new OutgoingCommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static Comment mapIncommingDtoToComment(IncomingCommentDto incomingCommentDto) {
        return new Comment(
                null,
                incomingCommentDto.getText(),
                null,
                null,
                LocalDateTime.now());
    }

}
