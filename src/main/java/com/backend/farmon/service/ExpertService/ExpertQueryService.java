package com.backend.farmon.service.ExpertService;

import com.backend.farmon.domain.enums.Role;
import com.backend.farmon.dto.estimate.EstimateRequestDTO;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;
import com.backend.farmon.dto.expert.ExpertListResponse;
import com.backend.farmon.dto.user.ExchangeResponse;
import org.springframework.data.domain.Pageable;

public interface ExpertQueryService {
    ExpertListResponse.ExpertProfileListDTO getExpertList(String crop, String area, Integer page);
}
