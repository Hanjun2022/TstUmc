package com.backend.farmon.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


public class PostRequestDTO {


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "게시글 저장 DTO")
    public static class PostSaveRequestDto {


        @Schema(description = "게시글 제목", required = true)
        @NotEmpty(message = "제목을 입력해주세요.")
        private String postTitle;

        @Schema(description = "게시글 내용", required = true)
        @NotEmpty(message = "내용을 입력해주세요.")
        private String postContent;

        @Schema(description = "좋아요 수", defaultValue = "0")
        private int postLike = 0;

        // 여기는 확신 못함
        @Schema(description = "해시태그")
        private String hashTag;



        @Schema(description = "좋아요수" ,defaultValue = "1")
        private int liked=0;

        @Schema(description = "게시판 5개")
        @Enumerated(value = EnumType.STRING)
        private PostType postType;



        @Schema(description = "작성 시간", example = "2025-01-01T12:00:00")
        private String createdAt;



    }
    @Getter
    @Setter
    public static class PostPagingRequestDto {

        private int page;   // 페이지 번호
        private int size;   // 페이지 크기
        private String sort; // 정렬 기준
    }


}