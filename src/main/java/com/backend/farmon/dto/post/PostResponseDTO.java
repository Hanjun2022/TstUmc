package com.backend.farmon.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "게시글 목록 응답 DTO")
public class PostResponseDTO {


    @Schema(description = "게시글 목록")
    private List<PostSummary> posts;

    @Schema(description = "페이지네이션 정보")
    private Pagination pagination;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "게시글 요약 정보")
    public static class PostSummary {
        @Schema(description = "게시글 ID", example = "1")
        private Long postId;

        @Schema(description = "게시글 제목", example = "게시글 제목 예시")
        private String postTitle;

        @Schema(description = "게시글 내용 요약", example = "게시글 내용 요약...")
        private String postSummary;

        @Schema(description = "게시글 좋아요 수", example = "25")
        private int postLike;

        @Schema(description = "해시태그 내용", example = "작물1")
        private String hashTag;

        @Schema(description = "게시글 댓글 수", example = "10")
        private int postComment;

        @Schema(description = "작성 시간", example = "2025-01-01T12:00:00")
        private String createdAt;

        @Schema(description = "게시글 유형", example = "ALL 또는 KNOWHOW")
        @Enumerated(value = EnumType.STRING)
        private PostType postType;


    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "페이지네이션 정보")
    public static class Pagination {
        @Schema(description = "현재 페이지 번호", example = "1")
        private int currentPage;

        @Schema(description = "전체 페이지 수", example = "10")
        private int totalPages;

        @Schema(description = "페이지당 게시글 수", example = "20")
        private int postsPerPage;
    }
}