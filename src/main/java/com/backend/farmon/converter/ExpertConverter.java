package com.backend.farmon.converter;

import com.backend.farmon.domain.*;
import com.backend.farmon.domain.enums.Role;
import com.backend.farmon.dto.expert.*;
import com.backend.farmon.dto.user.SignupRequest;
import com.backend.farmon.dto.user.SignupResponse;
import com.backend.farmon.repository.EstimateRepository.EstimateRepository;
import com.backend.farmon.service.AWS.S3Service;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExpertConverter {

    private final EstimateRepository estimateRepository;

    // 전문가 경력 엔티티 생성
    public static ExpertCareer toExpertCareer(ExpertCareerRequest.ExpertCareerPostDTO request) {
        return ExpertCareer.builder()
                .title(request.getTitle())
                .startYear(request.getStartYear())
                .startMonth(request.getStartMonth())
                .endYear(request.getEndYear())
                .endMonth(request.getEndMonth())
                .isOngoing(request.getIsOngoing())
                .detailContent1(request.getDetailContent1())
                .detailContent2(request.getDetailContent2())
                .detailContent3(request.getDetailContent3())
                .detailContent4(request.getDetailContent4())
                .build();
    }

    // 전문가 경력 POST 응답 DTO 생성
    public static ExpertCareerResponse.PostExpertCareerResultDTO toExpertCareerPostResultDTO(ExpertCareer expertCareer) {
        return ExpertCareerResponse.PostExpertCareerResultDTO.builder()
                .expertCareerId(expertCareer.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 전문가 경력 GET 응답 DTO 생성
    public static ExpertCareerResponse.GetExpertCareerResultDTO toExpertCareerGetResultDTO(ExpertCareer request) {
        return ExpertCareerResponse.GetExpertCareerResultDTO.builder()
                .expertId(request.getExpert().getId())
                .title(request.getTitle())
                .startYear(request.getStartYear())
                .startMonth(request.getStartMonth())
                .endYear(request.getEndYear())
                .endMonth(request.getEndMonth())
                .isOngoing(request.getIsOngoing())
                .detailContent1(request.getDetailContent1())
                .detailContent2(request.getDetailContent2())
                .detailContent3(request.getDetailContent3())
                .detailContent4(request.getDetailContent4())
                .build();
    }

    // 전문가 경력 엔티티 업데이트
    public static ExpertCareer updateExpertCareer(ExpertCareer expertCareer, ExpertCareerRequest.ExpertCareerPostDTO expertCareerPostDTO) {
        // 수정하는거라 빌더가 아니라 기존 엔티티 정보만 수정
        if (expertCareerPostDTO.getTitle() != null) {
            expertCareer.setTitle(expertCareerPostDTO.getTitle());
        }
        if (expertCareerPostDTO.getStartYear() != null) {
            expertCareer.setStartYear(expertCareerPostDTO.getStartYear());
        }
        if (expertCareerPostDTO.getStartMonth() != null) {
            expertCareer.setStartMonth(expertCareerPostDTO.getStartMonth());
        }
        if (expertCareerPostDTO.getEndYear() != null) {
            expertCareer.setEndYear(expertCareerPostDTO.getEndYear());
        }
        if (expertCareerPostDTO.getEndMonth() != null) {
            expertCareer.setEndMonth(expertCareerPostDTO.getEndMonth());
        }
        if (expertCareerPostDTO.getIsOngoing() != null) {
            expertCareer.setIsOngoing(expertCareerPostDTO.getIsOngoing());
        }
        if (expertCareerPostDTO.getDetailContent1() != null) {
            expertCareer.setDetailContent1(expertCareerPostDTO.getDetailContent1());
        }
        if (expertCareerPostDTO.getDetailContent2() != null) {
            expertCareer.setDetailContent2(expertCareerPostDTO.getDetailContent2());
        }
        if (expertCareerPostDTO.getDetailContent3() != null) {
            expertCareer.setDetailContent3(expertCareerPostDTO.getDetailContent3());
        }
        if (expertCareerPostDTO.getDetailContent4() != null) {
            expertCareer.setDetailContent4(expertCareerPostDTO.getDetailContent4());
        }

        return expertCareer;
    }

    // 전문가 대표서비스 수정 응답 DTO 생성
    public static ExpertProfileResponse.ResultUpdateSpecialtyDTO updateSpecialtyDTO(Expert request) {
        return ExpertProfileResponse.ResultUpdateSpecialtyDTO.builder()
                .expertId(request.getId())
                .cropCategory(request.getCrop().getCategory())
                .crop(request.getCrop().getName())
                .serviceDetail1(request.getServiceDetail1())
                .serviceDetail2(request.getServiceDetail2())
                .serviceDetail3(request.getServiceDetail3())
                .serviceDetail4(request.getServiceDetail4())
                .build();
    }

    // 전문가 활동지역 수정 응답 DTO 생성
    public static ExpertProfileResponse.ResultUpdateAreaDTO updateAreaDTO(Expert request) {
        return ExpertProfileResponse.ResultUpdateAreaDTO.builder()
                .expertId(request.getId())
                .areaName(request.getArea().getAreaName())
                .areaNameDetail(request.getArea().getAreaNameDetail())
                .availableRange(request.getAvailableRange())
                .isAvailableEverywhere(request.getIsAvailableEverywhere())
                .isExcludeIsland(request.getIsExcludeIsland())
                .build();
    }

    // 전문가 프로필 리스트 조회
    public static ExpertListResponse.ExpertProfileListDTO expertProfileListDTO(Page<Expert> expertList) {
        List<ExpertListResponse.ExpertProfileViewDTO> expertProfileViewDTOList = expertList.stream()
                .map(ExpertConverter::expertProfileViewDTO).collect(Collectors.toList());

        return ExpertListResponse.ExpertProfileListDTO.builder()
                .isLast(expertList.isLast())
                .isFirst(expertList.isFirst())
                .totalPage(expertList.getTotalPages())
                .totalElements(expertList.getTotalElements())
                .listSize(expertProfileViewDTOList.size())
                .expertProfileList(expertProfileViewDTOList)
                .build();

    }

    public static ExpertListResponse.ExpertProfileViewDTO expertProfileViewDTO(Expert expert) {
        return ExpertListResponse.ExpertProfileViewDTO.builder()
                .expertId(expert.getId())
                .profileImg(expert.getProfileImageUrl())
                .name(expert.getUser().getUserName())
                .rate(expert.getRating())
                .career(expert.getCareerYears())
                .expertDescription(expert.getExpertDescription())
                .expertCropCategory(expert.getCrop().getCategory())
                .expertCropDetail(expert.getCrop().getName())
                .expertLocationCategory(expert.getArea().getAreaName())
                .expertLocationDetail(expert.getArea().getAreaNameDetail())
                .build();
    }

    // 특정 전문가 포트폴리오 페이지 조회
    public ExpertProfileResponse.ExpertProfileDTO getProfilePage(Expert expert) {

        List<ExpertProfileResponse.PortfolioDetailDTO> portfolioDetailDTOList = expert.getPortfolioList().stream()
                .map(ExpertConverter::portfolioViewDTO).collect(Collectors.toList());

        List<ExpertProfileResponse.ExpertCareerDTO> expertCareerDTOList = expert.getExpertCareerList().stream()
                .map(ExpertConverter::expertCareerViewDTO).collect(Collectors.toList());

        // Estimate 수를 카운트
        long consultingCount = estimateRepository.countByExpert(expert);

        return ExpertProfileResponse.ExpertProfileDTO.builder()
                .profileImg(expert.getProfileImageUrl())
                .name(expert.getUser().getUserName())
                 // .nickName(expert.getNickName)  닉네임 항목 추가시 수정
                .expertDescription(expert.getExpertDescription())
                .rate(expert.getRating())
                // .reviewCount() 리뷰 추가시 수정
                .consultingCount(consultingCount)
                .careers(expertCareerDTOList)
                .additionalInformation(expert.getAdditionalInformation())
                .expertCropCategory(expert.getCrop().getCategory())
                .expertCropDetail(expert.getCrop().getName())
                .serviceDetail1(expert.getServiceDetail1())
                .serviceDetail2(expert.getServiceDetail2())
                .serviceDetail3(expert.getServiceDetail3())
                .serviceDetail4(expert.getServiceDetail4())
                .portfolio(portfolioDetailDTOList)
                .expertLocationCategory(expert.getArea().getAreaName())
                .expertLocationDetail(expert.getArea().getAreaNameDetail())
                .availableRange(expert.getAvailableRange())
                .isAvailableEverywhere(expert.getIsAvailableEverywhere())
                .isExcludeIsland(expert.getIsExcludeIsland())
                .build();
    }


    public static ExpertProfileResponse.PortfolioDetailDTO portfolioViewDTO(Portfolio portfolio) {
        return ExpertProfileResponse.PortfolioDetailDTO.builder()
                .thumbnailImg(portfolio.getThumbnailImg())
                .name(portfolio.getTitle())
                .build();
    }


    public static ExpertProfileResponse.ExpertCareerDTO expertCareerViewDTO(ExpertCareer expertCareer) {
        return ExpertProfileResponse.ExpertCareerDTO.builder()
                .title(expertCareer.getTitle())
                .startYear(expertCareer.getStartYear())
                .startMonth(expertCareer.getStartMonth())
                .endYear(expertCareer.getEndYear())
                .endMonth(expertCareer.getEndMonth())
                .isOngoing(expertCareer.getIsOngoing())
                .detailContent1(expertCareer.getDetailContent1())
                .detailContent2(expertCareer.getDetailContent2())
                .detailContent3(expertCareer.getDetailContent3())
                .detailContent4(expertCareer.getDetailContent4())
                .build();
    }
}
