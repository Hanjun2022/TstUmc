package com.backend.farmon.service.EstimateService;

import com.backend.farmon.dto.estimate.EstimateRequestDTO;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;

public interface EstimateQueryService {
    //1) 견적서 상세정보
    EstimateResponseDTO.DetailDTO getEstimateDetail(Long id);

    // 2) 전문가-견적찾기-전문가작물 해당 견적만
    EstimateResponseDTO.ListDTO getEstimateListByExpertCropId(Long expertId, int page);

    // 3) 전문가-견적찾기-작물카테고리 해당 견적만
    EstimateResponseDTO.ListDTO getEstimateListByCropCategory(String cropCategory, int page);

    // 4) 전문가-견적찾기-세부작물 이름 해당 견적만
    EstimateResponseDTO.ListDTO getEstimateListByCropName(String cropName, int page);

    // 5) 전문가- 내 견적찾기- 전문가 ID 해당 견적 모두
    EstimateResponseDTO.ListDTO getAllEstimateListByExpertId(Long expertId, int page);

    // 6) 전문가- 내 견적찾기- 전문가 ID 해당 견적 중 완료된 견적만
    EstimateResponseDTO.ListDTO getCompletedEstimateListByExpertId(Long expertId, int page);

    // 7) 농업인- 내 견적찾기- 농업인 ID 해당 견적 모두
    EstimateResponseDTO.ListDTO getAllEstimateListByUserId(Long userId, int page);

    // 8) 농업인- 내 견적찾기- 농업인 ID 해당 견적 중 완료된 견적만
    EstimateResponseDTO.ListDTO getCompletedEstimateListByUserId(Long userId, int page);

    // 9) 농업인- 등록된 견적 중 최신순 상위 5개 조회
    EstimateResponseDTO.ListDTO getRecent5EstimateListByUserId(Long userId);

    // 10) 전문가-견적찾기-필터링 견적서카테고리, 예산범위, 지역 으로 필터링
    EstimateResponseDTO.FilteredListDTO searchEstimateListByFilter(EstimateRequestDTO.FilterDTO requestDTO, int page);

    // 10) 전문가-견적찾기-필터링 견적서카테고리, 예산범위, 지역 으로 필터링2
    EstimateResponseDTO.FilteredListDTO searchEstimateListByFilter2(Long expertId, String cropCategory, String cropName, EstimateRequestDTO.FilterDTO requestDTO, int page);
}
