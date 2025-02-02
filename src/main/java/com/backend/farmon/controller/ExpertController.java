package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.ExpertCareerHandler;
import com.backend.farmon.apiPayload.exception.handler.ExpertHandler;
import com.backend.farmon.converter.ExpertConverter;
import com.backend.farmon.domain.Expert;
import com.backend.farmon.domain.ExpertCareer;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;
import com.backend.farmon.dto.expert.*;
import com.backend.farmon.repository.ExpertCareerRepository.ExpertCareerRepository;
import com.backend.farmon.repository.ExpertReposiotry.ExpertRepository;
import com.backend.farmon.service.AWS.S3Service;
import com.backend.farmon.service.ExpertService.ExpertCommandService;
import com.backend.farmon.service.ExpertService.ExpertQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "전문가 정보", description = "전문가 관련 정보 CRUD API")
@RestController
@RequiredArgsConstructor
public class ExpertController {

    private final ExpertCommandService expertCommandService;
    private final ExpertCareerRepository expertCareerRepository;
    private final ExpertConverter expertConverter;
    private final ExpertRepository expertRepository;
    private final ExpertQueryService expertQueryService;
    private final S3Service s3Service;

    // 전문가 내 프로필 페이지 조회
    @GetMapping("/api/expert/{expert-id}")
    @Operation(summary = "전문가 내 프로필 페이지 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    public ApiResponse<ExpertProfileResponse.ExpertProfileDTO> getExpertProfilePage(
            @PathVariable(name = "expert-id") Long expertId) {

        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new ExpertHandler(ErrorStatus.EXPERT_NOT_FOUND));

        return ApiResponse.onSuccess(expertConverter.getProfilePage(expert));
    }

    // 전문가 경력 등록
    @PostMapping("/api/expert/{expert-id}/career")
    @Operation(summary = "전문가 경력 등록 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "expert-id", description = "경력을 등록하려는 전문가의 id", required = true),
    })
    public ApiResponse<ExpertCareerResponse.PostExpertCareerResultDTO> postExpertCareer(
            @RequestBody @Valid ExpertCareerRequest.ExpertCareerPostDTO expertCareerPostDTO,
            @PathVariable(name = "expert-id") Long expertId) {
        ExpertCareer expertCareer = expertCommandService.postExpertCareer(expertId, expertCareerPostDTO);
        return ApiResponse.onSuccess(ExpertConverter.toExpertCareerPostResultDTO(expertCareer));
    }

    // 전문가 특정 경력 조회
    @GetMapping("/api/expert/career/{career-id}")
    @Operation(summary = "전문가 특정 경력 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "career-id", description = "조회하려는 경력의 id", required = true)
    })
    public ApiResponse<ExpertCareerResponse.GetExpertCareerResultDTO> getExpertCareer(
            @PathVariable(name = "career-id") Long careerId) {
        ExpertCareer expertCareer = expertCareerRepository.findById(careerId)
                .orElseThrow(() -> new ExpertCareerHandler(ErrorStatus.EXPERT_CAREER_NOT_FOUND));

        return ApiResponse.onSuccess(ExpertConverter.toExpertCareerGetResultDTO(expertCareer));
    }

    // 전문가 특정 경력 편집
    @PatchMapping("/api/expert/career/{career-id}")
    @Operation(summary = "전문가 특정 경력 편집 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "career-id", description = "편집하려는 경력의 id", required = true)
    })
    public ApiResponse<ExpertCareerResponse.GetExpertCareerResultDTO> updateExpertCareer(
            @RequestBody ExpertCareerRequest.ExpertCareerPostDTO expertCareerPostDTO,
            @PathVariable(name = "career-id") Long careerId) {
        ExpertCareer expertCareer = expertCareerRepository.findById(careerId)
                .orElseThrow(() -> new ExpertCareerHandler(ErrorStatus.EXPERT_CAREER_NOT_FOUND));

        ExpertCareer updatedExpertCareer = ExpertConverter.updateExpertCareer(expertCareer, expertCareerPostDTO);
        expertCareerRepository.save(updatedExpertCareer);
        return ApiResponse.onSuccess(ExpertConverter.toExpertCareerGetResultDTO(updatedExpertCareer));
    }

    // 전문가 특정 경력 삭제
    @DeleteMapping("/api/expert/career/{career-id}")
    @Operation(summary = "전문가 특정 경력 삭제 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "career-id", description = "삭제하려는 경력의 id", required = true)
    })
    public ApiResponse<String> deleteExpertCareer(@PathVariable(name = "career-id") Long careerId) {
        ExpertCareer expertCareer = expertCareerRepository.findById(careerId)
                .orElseThrow(() -> new ExpertCareerHandler(ErrorStatus.EXPERT_CAREER_NOT_FOUND));
        try {
            expertCareerRepository.delete(expertCareer);
            return ApiResponse.onSuccess("전문가 경력이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ApiResponse.onFailure("ERROR_DELETE_EXPERT_CAREER","전문가 경력 삭제에 실패했습니다.",null);
        }
    }

    // 전문가 추가정보 편집
    @PatchMapping("/api/expert/{expert-id}/detail")
    @Operation(summary = "전문가 추가정보 편집 API", description = "100자 이내의 내용을 적어주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "expert-id", description = "추가정보를 편집하려는 전문가의 id", required = true),
    })
    public ApiResponse<String> updateExpertDetail(
            @Valid @RequestBody ExpertDetailRequest.ExpertDetailPostDTO expertDetailPostDTO,
            @PathVariable(name = "expert-id") Long expertId) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new ExpertHandler(ErrorStatus.EXPERT_NOT_FOUND));

        expert.setAdditionalInformation(expertDetailPostDTO.getContent());
        expertRepository.save(expert);
        return ApiResponse.onSuccess("전문가 추가정보가 업데이트 되었습니다.");
    }

    // 전문가 프로필 이미지 설정 API
    @PutMapping(value = "/api/expert/{expert-id}/profileImg", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "전문가 프로필 이미지 설정 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "expert-id", description = "프로필 이미지를 등록하려는 전문가의 id", required = true)
    })
    public ApiResponse<String> setProfileImage(
            @RequestPart("file") MultipartFile multipartFile,
            @PathVariable("expert-id") Long expertId) {

        try {
            // 전문가의 프로필 이미지 설정
            String imageUrl = s3Service.putProfImage(expertId, multipartFile);

            // 프로필 이미지 업로드 성공시 응답 반환
            if (imageUrl != null) {
                return ApiResponse.onSuccess("프로필 이미지가 성공적으로 업로드되었습니다." + imageUrl);
            } else {
                return ApiResponse.onFailure("ERROR_UPLOAD_PROFILE_IMG","프로필 이미지 업로드에 실패하였습니다.",null);
            }
        } catch (Exception e) {
            return ApiResponse.onFailure("ERROR_UPLOAD_PROFILE_IMG","프로필 이미지 업로드에 실패하였습니다.",null);
        }
    }

    // 전문가 대표서비스 편집
    @PatchMapping("/api/expert/{expert-id}/specialty")
    @Operation(summary = "전문가 대표서비스 편집 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "expert-id", description = "대표서비스를 변경하려는 전문가의 id", required = true),
    })
    public ApiResponse<ExpertProfileResponse.ResultUpdateSpecialtyDTO> updateExpertSpecialty(
            @RequestBody ExpertProfileRequest.UpdateSpecialtyDTO updateSpecialtyDTO,
            @PathVariable(name = "expert-id") Long expertId) {
        Expert updatedExpert = expertCommandService.updateExpertSpecialty(expertId, updateSpecialtyDTO);
        return ApiResponse.onSuccess(ExpertConverter.updateSpecialtyDTO(updatedExpert));
    }

    // 전문가 활동지역 편집
    @PatchMapping("/api/expert/{expert-id}/area")
    @Operation(summary = "전문가 활동지역 편집 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "expert-id", description = "활동지역을 변경하려는 전문가의 id", required = true),
    })
    public ApiResponse<ExpertProfileResponse.ResultUpdateAreaDTO> updateExpertArea(
            @RequestBody ExpertProfileRequest.UpdateAreaDTO updateAreaDTO,
            @PathVariable(name = "expert-id") Long expertId) {
        Expert updatedExpert = expertCommandService.updateExpertArea(expertId, updateAreaDTO);
        return ApiResponse.onSuccess(ExpertConverter.updateAreaDTO(updatedExpert));
    }


    // 전문가 프로필 목록 조회
    @GetMapping("/api/expert/list")
    @Operation(
            summary = "전문가 프로필 목록 조회 API",
            description = "전문가 프로필 목록을 조회하는 API이며, 페이징을 포함합니다. " +
                    "서비스, 지역, 페이지를 query String 으로 주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "crop", description = "전문가 분야 필터링", required = false),
            @Parameter(name = "area", description = "전문가 지역 필터링", required = false),
            @Parameter(name = "page", description = "페이지 번호, 1부터 시작입니다.", example = "1", required = true)
    })
    public ApiResponse<ExpertListResponse.ExpertProfileListDTO> getExpertList (@RequestParam(name = "crop", required = false) String crop,
                                                                               @RequestParam(name = "area", required = false) String area,
                                                                               @RequestParam(name = "page") Integer page){
        ExpertListResponse.ExpertProfileListDTO response = expertQueryService.getExpertList(crop, area, page-1);

        return ApiResponse.onSuccess(response);
    }

}
