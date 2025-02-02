package com.backend.farmon.dto.expert;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.lang.Nullable;

public class ExpertCareerRequest {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "전문가 경력 등록 요청 DTO")
    public static class ExpertCareerPostDTO {
        @Schema(description = "경력 제목", example = "개인 컨설턴트")
        @NotBlank
        String title;

        @Schema(description = "시작 년도", example = "2009")
        Integer startYear;

        @Schema(description = "시작 월", example = "6")
        Integer startMonth;

        @Schema(description = "종료 년도", example = "2011")
        Integer endYear;

        @Schema(description = "종료 월", example = "1")
        Integer endMonth;

        @Schema(description = "경력 진행중 여부", example = "false")
        Boolean isOngoing;

        @Schema(description = "상세 설명1", example = "농업 기술 센터 방제 특강 강사 활동")
        String detailContent1;
        @Schema(description = "상세 설명2", example = "병해충 관리 업무")
        String detailContent2;
        @Schema(description = "상세 설명3")
        String detailContent3;
        @Schema(description = "상세 설명4")
        String detailContent4;
    }
}