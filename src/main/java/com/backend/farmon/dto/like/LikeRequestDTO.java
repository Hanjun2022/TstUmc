package com.backend.farmon.dto.like;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "좋아요 요청 DTO")
public class LikeRequestDTO {

    @Schema(description = "사용자Id", example = "xxx")
    private Long userId;  // 사용자 ID

    @Schema(description = "좋아요를 누를 대상의 ID (게시글 또는 댓글 ID)", example = "123")
    private Long postId;

    @Schema(description = "작성 시간", example = "2025-01-01T12:00:00")
    private String createdAt;

    @Schema(description = "수정 시간", example = "2025-01-01T12:30:00")
    private String updatedAt;


}
