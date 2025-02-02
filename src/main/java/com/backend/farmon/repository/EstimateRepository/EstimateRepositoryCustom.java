package com.backend.farmon.repository.EstimateRepository;

import com.backend.farmon.domain.Estimate;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstimateRepositoryCustom {
    Page<Estimate> findFilteredEstimates(String estimateCategory,String budget, String areaName, String areaNameDetail, Pageable pageable);

    Page<Estimate> findFilteredEstimates2(Long expertId, String cropCategory, String cropName, String estimateName,String budget, String areaName, String areaNameDetail, Pageable pageable);

}
