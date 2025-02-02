package com.backend.farmon.converter;

import com.backend.farmon.domain.Area;
import com.backend.farmon.domain.Crop;
import com.backend.farmon.domain.Estimate;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.estimate.EstimateRequestDTO;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EstimateConverter {

    // createDTO -> Estimate Entity
    public Estimate toEntity(EstimateRequestDTO.CreateDTO requestDTO, User user, Crop crop, Area area) {
        return Estimate.builder()
                .category(requestDTO.getCategory())
                .area(area)
                .budget(requestDTO.getBudget())
                .title(requestDTO.getTitle())
                .body(requestDTO.getBody())
                .status(0) // 기본 상태
                .user(user)
                .crop(crop)
                .build();

    }
    // Estimate -> CreateResponseDTO
    public EstimateResponseDTO.CreateDTO toCreateResponseDTO(Estimate estimate) {
        return EstimateResponseDTO.CreateDTO.builder()
                .estimateId(estimate.getId())
                .userId(estimate.getUser().getId())
                .build();
    }

    // Estimate -> UpdateResponseDTO
    public EstimateResponseDTO.UpdateDTO toUpdateResponseDTO(Long estimateId, Estimate estimate) {
        return EstimateResponseDTO.UpdateDTO.builder()
                .estimateId(estimateId)
                .userId(estimate.getUser().getId())
                .build();
    }

    // Estimate -> DeleteResponseDTO
    public EstimateResponseDTO.DeleteDTO toDeleteResponseDTO(Long estimateId) {
        return EstimateResponseDTO.DeleteDTO.builder()
                .estimateId(estimateId)
                .deleted(true)
                .build();
    }

    // Estimate -> DetialResponseDTO
    public EstimateResponseDTO.DetailDTO toDetailDTO(Estimate estimate) {
        return EstimateResponseDTO.DetailDTO.builder()
                .estimateId(estimate.getId())
                .userId(estimate.getUser().getId())
                .userName(estimate.getUser().getUserName())
                .cropName(estimate.getCrop().getName())
                .cropCategory(estimate.getCrop().getCategory())
                .category(estimate.getCategory())
                .areaName(estimate.getArea().getAreaName())
                .areaNameDetail(estimate.getArea().getAreaNameDetail())
                .budget(estimate.getBudget())
                .title(estimate.getTitle())
                .body(estimate.getBody())
                .createdDate(estimate.getCreatedAt().toLocalDate())
                .build();
    }

    //Page<Estimate> -> FilteredListDTO
    public EstimateResponseDTO.FilteredListDTO toFilteredListDTO(Page<Estimate> estimatePage, String estimateCategory, String budget, String areaName, String areaNameDetail){
        // 미리보기 리스트로 변환
        List<EstimateResponseDTO.PreviewDTO> previewDTOList = estimatePage.getContent().stream()
                .map(this::toPreviewDTO)
                .collect(Collectors.toList());

        // ListDTO 구성
        return EstimateResponseDTO.FilteredListDTO.builder()
                .listSize(previewDTOList.size())
                .totalPage(estimatePage.getTotalPages())
                .totalElements(estimatePage.getTotalElements())
                .currentPage(estimatePage.getNumber() + 1)
                .isFirst(estimatePage.isFirst())
                .isLast(estimatePage.isLast())
                .estimateCategory(estimateCategory)
                .budget(budget)
                .areaName(areaName)
                .areaNameDetail(areaNameDetail)
                .estimateList(previewDTOList)
                .build();
    }

    //Page<Estimate> -> ListDTO
    public EstimateResponseDTO.ListDTO toListDTO(Page<Estimate> estimatePage){
        // 미리보기 리스트로 변환
        List<EstimateResponseDTO.PreviewDTO> previewDTOList = estimatePage.getContent().stream()
                .map(this::toPreviewDTO)
                .collect(Collectors.toList());

        // ListDTO 구성
        return EstimateResponseDTO.ListDTO.builder()
                .listSize(previewDTOList.size())
                .totalPage(estimatePage.getTotalPages())
                .totalElements(estimatePage.getTotalElements())
                .currentPage(estimatePage.getNumber() + 1)
                .isFirst(estimatePage.isFirst())
                .isLast(estimatePage.isLast())
                .estimateList(previewDTOList)
                .build();
    }
    //List<Estimate> -> ListDTO
    public EstimateResponseDTO.ListDTO toListDTO(List<Estimate> estimateList){
        // 미리보기 리스트로 변환
        List<EstimateResponseDTO.PreviewDTO> previewDTOList = estimateList.stream()
                .map(this::toPreviewDTO)
                .collect(Collectors.toList());

        // ListDTO 구성
        return EstimateResponseDTO.ListDTO.builder()
                .listSize(previewDTOList.size())
                .estimateList(previewDTOList)
                .build();
    }

    //Estimate 엔티티 -> PreviewDTO
    private EstimateResponseDTO.PreviewDTO toPreviewDTO(Estimate estimate) {
        return EstimateResponseDTO.PreviewDTO.builder()
                .estimateId(estimate.getId())
                .title(estimate.getTitle())
                .cropName(estimate.getCrop().getName())
                .cropCategory(estimate.getCrop().getCategory())
                .estimateCategory(estimate.getCategory())
                .areaName(estimate.getArea().getAreaName())
                .areaNameDetail(estimate.getArea().getAreaNameDetail())
                .budget(estimate.getBudget())
                .status(estimate.getStatus())
                .createdAt(estimate.getCreatedAt().toLocalDate())
                .build();
    }

}
