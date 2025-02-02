package com.backend.farmon.converter;

import com.backend.farmon.domain.Comment;
import com.backend.farmon.domain.Expert;
import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.Comment.CommentRequestDTO;
import com.backend.farmon.dto.Comment.CommentResponseDTO;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class CommentConverter {

    // 댓글은 사용자로 달고 만약 전문가 등록된 작물이 있다면 이름을 등록

    // 부모 댓글 엔티티 -> 부모 댓글 응답 DTO
    public static CommentResponseDTO.CommentParentResponseDto toParentResponseDto(Comment comment) {
        return CommentResponseDTO.CommentParentResponseDto.builder()
                .commentId(comment.getId())
                .commentContent(comment.getCommentContent())
                .userId(comment.getUser() != null ? comment.getUser().getId() : null)  // 전문가 ID
                .expertCategory(comment.getUser() != null ? String.valueOf(comment.getUser().getExpert().getCrop()) : null)  // 전문가의 Crop
                .childComments(comment.getChildren().stream()
                        .map(CommentConverter::toChildResponseDto)
                        .collect(Collectors.toList()))
                .createdAt(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    // 대댓글 엔티티 -> 대댓글 응답 DTO
    public static CommentResponseDTO.CommentChildResponseDto toChildResponseDto(Comment comment) {
        return CommentResponseDTO.CommentChildResponseDto.builder()
                .commentId(comment.getId())
                .commentContent(comment.getCommentContent())
                .userId(comment.getUser() != null ? comment.getUser().getId() : null)  // 전문가 ID
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)  // 부모 댓글 ID
                .createdAt(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    // 부모 댓글 저장 요청 DTO -> 부모 댓글 엔티티
    public static Comment toParentSaveEntity(CommentRequestDTO.CommentSaveParentRequestDto requestDto, User user, Post post) {
        return Comment.builder()
                .commentContent(requestDto.getCommentContent())
                .authorName(user.getUserName())  // 전문가의 사용자명
                .expertCategory(String.valueOf(user.getExpert().getCrop()))  // 전문가의 Crop
                .post(post)
                .build();
    }

    // 대댓글 저장 요청 DTO -> 대댓글 엔티티
    public static Comment toChildCommentEntity(CommentRequestDTO.CommentSaveChildRequestDto requestDto, User user, Post post, Comment parentComment) {
        return Comment.builder()
                .commentContent(requestDto.getCommentContent())
                .authorName(user.getUserName())  // 사용자의 사용자명
                .post(post)
                .parent(parentComment)
                .build();
    }

    // 대댓글 저장 요청 DTO -> 대댓글 응답 DTO
    public static CommentResponseDTO.CommentChildResponseDto toChildResponseDto(CommentRequestDTO.CommentSaveChildRequestDto requestDto, Comment comment) {
        return CommentResponseDTO.CommentChildResponseDto.builder()
                .commentId(comment.getId())
                .commentContent(comment.getCommentContent())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)  // 부모 댓글 ID
                .build();
    }

    // 부모 댓글 리스트 -> 부모 댓글 응답 DTO 리스트
    public static List<CommentResponseDTO.CommentParentResponseDto> toParentResponseDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentConverter::toParentResponseDto)
                .collect(Collectors.toList());
    }

    // 부모 댓글 리스트 -> 커서 페이징 응답 DTO
    public static CommentResponseDTO.CommentCursorPagingResponseDto toCursorPagingResponseDto(List<Comment> comments, boolean hasNext) {
        Long lastCommentId = comments.isEmpty() ? null : comments.get(comments.size() - 1).getId();
        return CommentResponseDTO.CommentCursorPagingResponseDto.builder()
                .parentComments(toParentResponseDtoList(comments))
                .hasNext(hasNext)
                .lastCommentId(lastCommentId)
                .build();
    }
}
