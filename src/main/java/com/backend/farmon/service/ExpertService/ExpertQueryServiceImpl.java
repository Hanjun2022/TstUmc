package com.backend.farmon.service.ExpertService;

import com.backend.farmon.converter.EstimateConverter;
import com.backend.farmon.converter.ExpertConverter;
import com.backend.farmon.domain.Estimate;
import com.backend.farmon.domain.Expert;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;
import com.backend.farmon.dto.expert.ExpertListResponse;
import com.backend.farmon.repository.EstimateRepository.EstimateRepository;
import com.backend.farmon.repository.ExpertReposiotry.ExpertRepository;
import com.backend.farmon.service.UserService.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExpertQueryServiceImpl implements ExpertQueryService{

    private final ExpertRepository expertRepository;

    @Override
    public ExpertListResponse.ExpertProfileListDTO getExpertList(String crop, String area, Integer page) {

        Page<Expert> expertPage = expertRepository.findFilteredExperts(crop, area, PageRequest.of(page,9));

        return ExpertConverter.expertProfileListDTO(expertPage);
    }

}