package com.backend.farmon.dto.Comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class CommentRequestDTO {


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "부모 댓글 저장 DTO")
    public static class CommentSaveParentRequestDto {

        @Schema(description = "댓글 내용", example = "이것은 댓글입니다.", required = true)
        private String commentContent;

        @Schema(description = "작성자 유저 아이디", example = "samsunggood", required = true)
        private Long userId;

        @Schema(description = "게시글 아이디", example = "hjunew", required = true)
        private Long postId;


    }


    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "대댓글 저장 DTO")
    public static class CommentSaveChildRequestDto {

        @Schema(description = "댓글을 저장하는 회원 아이디", example = "1", required = true)
        private Long userId;

        @Schema(description = "대댓글 저장 내용", required = true, example = "This is a child comment")
        @NotNull
        private String commentContent;

        @Schema(description = "부모 댓글 ID", example = "1", required = true)
        private Long parentCommentId;
    }



}