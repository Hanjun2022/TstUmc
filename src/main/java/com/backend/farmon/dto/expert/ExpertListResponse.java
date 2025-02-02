package com.backend.farmon.dto.expert;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class ExpertListResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "전문가 프로필 목록")
    public static class ExpertProfileListDTO {
        @Schema(description = "전문가 프로필 목록")
        List<ExpertProfileViewDTO> expertProfileList;

        @Schema(description = "목록 개수")
        Integer listSize;

        @Schema(description = "전체 페이지 수")
        Integer totalPage;

        @Schema(description = "전체 프로필 개수")
        Long totalElements;

        @Schema(description = "첫 페이지인지 확인")
        Boolean isFirst;

        @Schema(description = "마지막 페이지인지 확인")
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpertProfileViewDTO {
        @Schema(description = "전문가 아이디")
        Long expertId;

        @Schema(description = "프로필 이미지")
        String profileImg;

        @Schema(description = "이름")
        String name;

        @Schema(description = "평점")
        Float rate;

        @Schema(description = "경력")
        Integer career;

        @Schema(description = "전문가 한 줄 소개")
        String expertDescription;

        @Schema(description = "전문가 전문 분야 카테고리")
        String expertCropCategory;

        @Schema(description = "전문가 전문 분야 세부")
        String expertCropDetail;

        @Schema(description = "전문가 활동 위치 카테고리")
        String expertLocationCategory;

        @Schema(description = "전문가 활동 위치 세부")
        String expertLocationDetail;
    }
}
