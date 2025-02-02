package com.backend.farmon.service.EstimateService;

import com.backend.farmon.converter.EstimateConverter;
import com.backend.farmon.domain.Estimate;
import com.backend.farmon.domain.EstimateImage;
import com.backend.farmon.dto.estimate.EstimateRequestDTO;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;
import com.backend.farmon.repository.EstimateRepository.EstimateRepository;
import com.backend.farmon.repository.ExpertReposiotry.ExpertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EstimateQueryServiceImpl implements EstimateQueryService {

    private final EstimateRepository estimateRepository;
    private final EstimateConverter estimateConverter;
    private final ExpertRepository expertRepository;
    /**
     * Read(상세 조회) - 단건
     */
    @Override
    public EstimateResponseDTO.DetailDTO getEstimateDetail(Long estimateId) {
        // 1)견적 찾기
        Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id와 일치하는 견적서가 없습니다."));

        // 2) 이미지 URL 가져오기
        List<String> imageUrls = estimate.getEstimateImageList().stream()
                .map(EstimateImage::getImageUrl)
                .toList();

        // 3) DTO 변환
        EstimateResponseDTO.DetailDTO detailDTO = estimateConverter.toDetailDTO(estimate);
        detailDTO.setImageUrls(imageUrls);

        return detailDTO;

    }
    /**
     * Read(목록 조회) - 견적찾기 /전문가 매핑 작물로 견적서 찾기
     */
    @Override
    public EstimateResponseDTO.ListDTO getEstimateListByExpertCropId(Long expertId, int page) {
        // 1) 전문가
        Long cropId =  expertRepository.findById(expertId).get().getCrop().getId();

        if(cropId == null){
            return EstimateResponseDTO.ListDTO.builder().build();
        }

        // 2) 견적서를 페이징하여 조회
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Estimate> estimatePage = estimateRepository.findByCropIdAndStatus(cropId, pageable);

        // 3) 응답 객체로 변환
        return estimateConverter.toListDTO(estimatePage);
    }

    /**
     * Read(목록 조회) - 견적찾기 /작물카테고리 로 견적서 찾기
     */
    @Override
    public EstimateResponseDTO.ListDTO getEstimateListByCropCategory(String cropCategory, int page) {

        if(cropCategory == null){
            return EstimateResponseDTO.ListDTO.builder().build();
        }

        // 1) 견적서를 페이징하여 조회
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Estimate> estimatePage = estimateRepository.findByCropCategoryAndStatus(cropCategory, pageable);

        // 2) 응답 객체로 변환
        return estimateConverter.toListDTO(estimatePage);
    }

    /**
     * Read(목록 조회) - 견적찾기 /작물 ID로 견적서 찾기
     */
    @Override
    public EstimateResponseDTO.ListDTO getEstimateListByCropName(String cropName, int page) {

        if(cropName == null){
            return EstimateResponseDTO.ListDTO.builder().build();
        }

        // 1) 견적서를 페이징하여 조회
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Estimate> estimatePage = estimateRepository.findByCropNameAndStatus(cropName, pageable);

        // 2) 응답 객체로 변환
        return estimateConverter.toListDTO(estimatePage);
    }

    /**
     * Read(목록 조회) - 전문가 - 내견적 찾기/ 전문가 ID로 견적서 모두 찾기
     */
    @Override
    public EstimateResponseDTO.ListDTO getAllEstimateListByExpertId(Long expertId, int page) {
        if(expertId == null){
            return EstimateResponseDTO.ListDTO.builder().build();
        }
        // 1) 견적서를 페이징하여 조회;
        Pageable pageable = PageRequest.of(page -1, 9, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Estimate> estimatePage = estimateRepository.findAllEstimatesByExpertId(expertId, pageable);
        // 2) 응답 객체로 변환
        return estimateConverter.toListDTO(estimatePage);
    }

    /**
     * Read(목록 조회) - 전문가 - 내견적 찾기/ 전문가 ID로 견적서 중 완료된 견적만 찾기
     */
    @Override
    public EstimateResponseDTO.ListDTO getCompletedEstimateListByExpertId(Long expertId, int page) {
        if(expertId == null){
            return EstimateResponseDTO.ListDTO.builder().build();
        }
        // 1) 견적서를 페이징하여 조회;
        Pageable pageable = PageRequest.of(page -1, 9, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Estimate> estimatePage = estimateRepository.findCompletedEstimatesByExpertId(expertId, pageable);
        // 2) 응답 객체로 변환
        return estimateConverter.toListDTO(estimatePage);
    }

    /**
     * Read(목록 조회) - 농업인 - 내견적 찾기/ 농업인 ID로 견적서 모두 찾기
     */
    @Override
    public EstimateResponseDTO.ListDTO getAllEstimateListByUserId(Long userId, int page) {
        if (userId == null){
            return EstimateResponseDTO.ListDTO.builder().build();
        }
        // 1) 견적서를 페이징하여 조회;
        Pageable pageable = PageRequest.of(page -1, 30, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Estimate> estimatePage = estimateRepository.findAllEstimatesByUserId(userId, pageable);
        // 2) 응답 객체로 변환
        return estimateConverter.toListDTO(estimatePage);
    }

    /**
     * Read(목록 조회) - 농업인 - 내견적 찾기/ 농업인 ID로 견적서 중 완료된 견적만 찾기
     */
    @Override
    public EstimateResponseDTO.ListDTO getCompletedEstimateListByUserId(Long userId, int page) {
        if (userId == null){
            return EstimateResponseDTO.ListDTO.builder().build();
        }
        // 1) 견적서를 페이징하여 조회;
        Pageable pageable = PageRequest.of(page -1, 30, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Estimate> estimatePage = estimateRepository.findCompletedEstimatesByUserId(userId, pageable);
        // 2) 응답 객체로 변환
        return estimateConverter.toListDTO(estimatePage);
    }

    /**
     * 농업인 - 내 등록 견적 중 최신순 5개 조회
     */
    @Override
    public EstimateResponseDTO.ListDTO getRecent5EstimateListByUserId(Long userId) {

        if(userId == null) {
            return EstimateResponseDTO.ListDTO.builder().build();
        }
        // 1) 레포지토리에서 데이터 조회
        List<Estimate> estimateList = estimateRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId);
        // 2) 응답 객체로 변환
        return estimateConverter.toListDTO(estimateList);
    }

    /**
     * 필터링 조건에 따른 견적서 목록 조회
     *
     * @paramrequest 필터링 조건 DTO
     * @return 필터링된 견적서 목록
     */
    @Override
    public EstimateResponseDTO.FilteredListDTO searchEstimateListByFilter(EstimateRequestDTO.FilterDTO requestDTO, int page) {
        if(requestDTO == null) {
            return EstimateResponseDTO.FilteredListDTO.builder().build();
        };

        // Repository를 호출하여 조건에 맞는 견적서를 조회
        // 1) 레포지토리에서 데이터 조회
        Page<Estimate> estimatePage = estimateRepository.findFilteredEstimates(
                requestDTO.getEstimateCategory(),
                requestDTO.getBudget(),
                requestDTO.getAreaName(),
                requestDTO.getAreaNameDetail(),
                PageRequest.of(page -1, 10, Sort.by(Sort.Direction.DESC, "createdAt"))
        );

        return estimateConverter.toFilteredListDTO(estimatePage, requestDTO.getEstimateCategory(), requestDTO.getBudget(), requestDTO.getAreaName(), requestDTO.getAreaNameDetail());

    }

    @Override
    public EstimateResponseDTO.FilteredListDTO searchEstimateListByFilter2(Long expertId, String cropCategory, String cropName, EstimateRequestDTO.FilterDTO requestDTO, int page) {
        if(requestDTO == null) {
            return EstimateResponseDTO.FilteredListDTO.builder().build();
        };

        // Repository를 호출하여 조건에 맞는 견적서를 조회
        // 1) 레포지토리에서 데이터 조회
        Page<Estimate> estimatePage = estimateRepository.findFilteredEstimates2(
                expertId,
                cropCategory,
                cropName,
                requestDTO.getEstimateCategory(),
                requestDTO.getBudget(),
                requestDTO.getAreaName(),
                requestDTO.getAreaNameDetail(),
                PageRequest.of(page -1, 10, Sort.by(Sort.Direction.DESC, "createdAt"))
        );

        return estimateConverter.toFilteredListDTO(estimatePage, requestDTO.getEstimateCategory(), requestDTO.getBudget(), requestDTO.getAreaName(), requestDTO.getAreaNameDetail());

    }

}
