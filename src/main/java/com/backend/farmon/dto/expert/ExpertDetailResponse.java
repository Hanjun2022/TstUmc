package com.backend.farmon.dto.expert;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ExpertDetailResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "전문가 추가정보 등록 응답 DTO")
    public static class PostExpertDetailResultDTO {
        @Schema(description = "생성된 추가정보 아이디")
        Long expertDetailId;

        @Schema(description = "생성시간")
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "전문가 추가정보 조회 응답 DTO")
    public static class GetExpertDetailResultDTO {
        @Schema(description = "해당 전문가 ID")
        Long expertId;

        @Schema(description = "추가정보 내용")
        String content;
    }
}
