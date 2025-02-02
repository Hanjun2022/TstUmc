package com.backend.farmon.dto.expert;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class ExpertProfileResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpertProfileDTO {
        @Schema(description = "프로필 이미지")
        String profileImg;

        @Schema(description = "이름")
        String name;

        @Schema(description = "전문가 한 줄 소개")
        String expertDescription;

        @Schema(description = "평점")
        Float rate;

        @Schema(description = "리뷰 개수")
        Integer reviewCount;

        @Schema(description = "진행했던 컨설팅 수")
        Long consultingCount;

        @Schema(description = "전문가 경력 목록 리스트")
        List<ExpertCareerDTO> careers;

        @Schema(description = "전문가 추가정보")
        String additionalInformation;

        @Schema(description = "전문가 전문 분야 카테고리")
        String expertCropCategory;

        @Schema(description = "전문가 전문 분야 세부")
        String expertCropDetail;

        String serviceDetail1;
        String serviceDetail2;
        String serviceDetail3;
        String serviceDetail4;

        @Schema(description = "포트폴리오 목록 리스트")
        List<PortfolioDetailDTO> portfolio;

        @Schema(description = "전문가 활동 위치 카테고리")
        String expertLocationCategory;

        @Schema(description = "전문가 활동 위치 세부")
        String expertLocationDetail;

        @Schema(description = "활동 가능 범위")
        String availableRange;

        @Schema(description = "전국 어디든 가능 여부")
        Boolean isAvailableEverywhere;

        @Schema(description = "도서 지방 제외 여부")
        Boolean isExcludeIsland;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortfolioDetailDTO {
        @Schema(description = "포트폴리오 썸네일 이미지")
        String thumbnailImg;

        @Schema(description = "포트폴리오 제목")
        String name;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpertCareerDTO {
        @Schema(description = "전문가 경력 제목")
        String title;

        @Schema(description = "시작 일시 년도")
        Integer startYear;

        @Schema(description = "시작 일시 월")
        Integer startMonth;

        @Schema(description = "종료 일시 년도")
        Integer endYear;

        @Schema(description = "종료 일시 년도")
        Integer endMonth;

        @Schema(description = "진행중 여부")
        Boolean isOngoing;

        String detailContent1;
        String detailContent2;
        String detailContent3;
        String detailContent4;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpertDetailDTO {
        @Schema(description = "전문가 추가정보 내용")
        String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultUpdateSpecialtyDTO { // 대표 서비스 변경시 응답 DTO
        @Schema(description = "전문가 아이디")
        Long expertId;

        @Schema(description = "작물 카테고리")
        String cropCategory;

        @Schema(description = "작물 이름")
        String crop;

        @Schema(description = "대표 서비스 디테일1")
        String serviceDetail1;
        @Schema(description = "대표 서비스 디테일2")
        String serviceDetail2;
        @Schema(description = "대표 서비스 디테일3")
        String serviceDetail3;
        @Schema(description = "대표 서비스 디테일4")
        String serviceDetail4;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultUpdateAreaDTO { // 활동지역 변경시 응답 DTO
        @Schema(description = "전문가 아이디")
        Long expertId;

        @Schema(description = "시/도")
        String areaName;

        @Schema(description = "시/구")
        String areaNameDetail;

        @Schema(description = "활동 가능 범위")
        String availableRange;
        @Schema(description = "전국 어디든 가능 여부")
        Boolean isAvailableEverywhere;
        @Schema(description = "도서 지방 제외 여부")
        Boolean isExcludeIsland;
    }
}
