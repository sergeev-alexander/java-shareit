package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.dto.IncomingCommentDto;
import ru.practicum.shareit.comment.dto.OutgoingCommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentMapperTest {

    @Test
    void mapCommentToOutgoingDto() {
        Comment comment = new Comment(
                1L,
                "Comment text",
                null,
                new User(
                        2L,
                        "Author name",
                        "author@email.com"),
                LocalDateTime.of(2000, 1, 1, 1, 1, 1));
        OutgoingCommentDto result = CommentMapper.mapCommentToOutgoingDto(comment);
        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getText(), result.getText());
        assertEquals(comment.getCreated(), result.getCreated());
        assertEquals(comment.getAuthor().getName(), result.getAuthorName());
    }

    @Test
    void mapIncomingDtoToComment() {
        IncomingCommentDto incomingCommentDto = new IncomingCommentDto(
                null,
                "Comment text");
        Comment result = CommentMapper.mapIncommingDtoToComment(incomingCommentDto);
        assertEquals(incomingCommentDto.getText(), result.getText());
    }

}
