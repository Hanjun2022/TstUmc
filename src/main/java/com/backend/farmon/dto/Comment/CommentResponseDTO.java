package com.backend.farmon.dto.Comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class CommentResponseDTO {

    // 댓글 조회 응답 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "댓글 전체 조회 응답 DTO")
    public static class CommentResponseDto {
        @Schema(description = "부모 댓글 목록")
        private List<CommentParentResponseDto> parentComments; // 부모 댓글 리스트

        @Schema(description = "다음 페이지 여부", example = "true")
        private boolean hasNext;
    }

    // 부모 댓글 정보 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "부모 댓글 조회용 DTO")
    public static class CommentParentResponseDto {

        @Schema(description = "댓글 ID", example = "1")
        private Long commentId;

        @Schema(description = "부모 댓글 내용", example = "This is a parent comment")
        @NotNull
        private String commentContent;

        @Schema(description = "작성자 유저 아이디", example = "1")
        private Long userId;

        @Schema(description = "전문가", example = "곡물")
        private String expertCategory;

        @Schema(description = "대댓글 목록")
        private List<CommentChildResponseDto> childComments; // 대댓글 리스트 포함

        @Schema(description = "시간")
        private String createdAt;
    }

    // 댓글 삭제 성공 여부를 판단하는 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "댓글 삭제 여부 정보")
    public static class CommentDeleteDTO {
        @Schema(description = "삭제 여부 / 삭제에 성공했다면 true, 실패했다면 false", example = "true")
        Boolean isDeleteSuccess;
    }

    // 대댓글 정보 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "대댓글 조회용 DTO")
    public static class CommentChildResponseDto {
        @Schema(description = "작성자 유저 아이디", example = "1")
        private Long userId;

        @Schema(description = "대댓글 ID", example = "2")
        private Long commentId;

        @Schema(description = "대댓글 내용", example = "This is a child comment")
        @NotNull
        private String commentContent;

        @Schema(description = "부모댓글 ID", example = "2")
        private Long parentId;

        @Schema(description = "시간")
        private String createdAt;
    }

    // 커서 페이징 요청 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "댓글 커서 페이징 요청 DTO")
    public static class CommentCursorPagingRequestDto {

        @Schema(description = "마지막으로 조회된 댓글 ID (커서)", example = "50")
        private Long lastCommentId; // 마지막으로 조회된 댓글의 ID

        @Schema(description = "한 번에 조회할 댓글 수 (limit)", example = "10", required = true)
        private int limit;

        @Schema(description = "게시글 ID", example = "1", required = true)
        private Long postId; // 특정 게시글의 댓글을 조회하기 위해 필요
    }

    // 커서 페이징 응답 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "댓글 커서 페이징 응답 DTO")
    public static class CommentCursorPagingResponseDto {

        @Schema(description = "부모 댓글 목록")
        private List<CommentParentResponseDto> parentComments; // 부모 댓글 리스트

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        private boolean hasNext; // 다음 페이지가 있는지 여부

        @Schema(description = "마지막 댓글 ID", example = "60")
        private Long lastCommentId; // 현재 페이지에서 마지막으로 반환된 댓글 ID
    }
}
