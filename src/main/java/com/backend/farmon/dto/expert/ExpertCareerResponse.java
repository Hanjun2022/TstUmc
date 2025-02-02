package com.backend.farmon.dto.expert;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ExpertCareerResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "전문가 경력 등록 응답 DTO")
    public static class PostExpertCareerResultDTO {
        @Schema(description = "생성된 경력 아이디")
        Long expertCareerId;

        @Schema(description = "생성시간")
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "전문가 경력 조회 응답 DTO")
    public static class GetExpertCareerResultDTO {
        @Schema(description = "해당 전문가 ID")
        Long expertId;

        @Schema(description = "경력 제목")
        String title;

        @Schema(description = "시작 년도")
        Integer startYear;

        @Schema(description = "시작 월")
        Integer startMonth;

        @Schema(description = "종료 년도")
        Integer endYear;

        @Schema(description = "종료 월")
        Integer endMonth;

        @Schema(description = "경력 진행중 여부")
        Boolean isOngoing;

        @Schema(description = "상세 설명1")
        String detailContent1;
        @Schema(description = "상세 설명2")
        String detailContent2;
        @Schema(description = "상세 설명3")
        String detailContent3;
        @Schema(description = "상세 설명4")
        String detailContent4;
    }
}
