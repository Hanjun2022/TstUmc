package com.backend.farmon.service.EstimateService;


import com.backend.farmon.dto.estimate.EstimateRequestDTO;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.nio.channels.MulticastChannel;
import java.util.List;

public interface EstimateCommandService {

    // 1) Create(견적서 작성)
    EstimateResponseDTO.CreateDTO createEstimate (EstimateRequestDTO.CreateDTO requestDTO, List<MultipartFile> imageFiles);

    // 2) Update(견적서 수정)
    EstimateResponseDTO.UpdateDTO updateEstimate (
            Long estimateId, EstimateRequestDTO.UpdateDTO requestDTO);

    // 3) Delete(견적서 삭제)
    EstimateResponseDTO.DeleteDTO deleteEstimate (Long estimateId);
}
