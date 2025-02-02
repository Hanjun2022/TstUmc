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
@Schema(description = "좋아요 응답 DTO")
public class LikeResponseDTO {

    @Schema(description = "좋아요 유저의 ID", example = "1")
    private Long userId;

    @Schema(description = "좋아요 대상 ID", example = "xxx")
    private Long postId;

    @Schema(description = "좋아요 개수", example = "1")
    private int likeCount;

    @Schema(description = "사용자가 좋아요를 눌렀는지 여부", example = "true")
    private boolean isLikedByUser;

    @Schema(description = "작성 시간", example = "2025-01-01T12:00:00")
    private String createdAt;

    @Schema(description = "수정 시간", example = "2025-01-01T12:30:00")
    private String updatedAt;
}
