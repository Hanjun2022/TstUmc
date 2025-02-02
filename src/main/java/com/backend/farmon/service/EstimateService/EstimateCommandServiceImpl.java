package com.backend.farmon.service.EstimateService;

import com.backend.farmon.aws.s3.AmazonS3Manager;
import com.backend.farmon.aws.s3.UuidRepository;
import com.backend.farmon.converter.EstimateConverter;
import com.backend.farmon.domain.*;
import com.backend.farmon.dto.estimate.EstimateRequestDTO;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;
import com.backend.farmon.repository.AreaRepository.AreaRepository;
import com.backend.farmon.repository.CropRepository.CropRepository;
import com.backend.farmon.repository.EstimateImageRepository.EstimateImageRepository;
import com.backend.farmon.repository.EstimateRepository.EstimateRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EstimateCommandServiceImpl implements EstimateCommandService {
    private final UserRepository userRepository;
    private final EstimateRepository estimateRepository;
    private final CropRepository cropRepository;
    private final AreaRepository areaRepository;
    private final EstimateImageRepository estimateImageRepository;
    private final UuidRepository uuidRepository;
    private final AmazonS3Manager amazonS3Manager;
    private final EstimateConverter estimateConverter;

    private String extractS3KeyFromUrl(String imageUrl) {
        return imageUrl.substring(imageUrl.indexOf("estimate/"));
        // 예: https://s3.amazonaws.com/bucket-name/estimate/UUID_filename.jpg
        // -> estimate/UUID_filename.jpg (S3에서 삭제할 key)
    }

    /**
     * Create(견적서 작성)
     */

    @Override
    public EstimateResponseDTO.CreateDTO createEstimate(EstimateRequestDTO.CreateDTO requestDTO, List<MultipartFile> imageFiles) {
        // 1) Entity 생성
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID 입니다."));
        Crop crop = cropRepository.findByName(requestDTO.getCropName())
                .orElseThrow(() -> new IllegalArgumentException("유호하지 않은 작물 이름 입니다."));
        Area area = areaRepository.findByAreaNameAndAreaNameDetail(requestDTO.getAreaName(), requestDTO.getAreaNameDetail())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 지역 이름 입니다."));

        Estimate estimate = estimateConverter.toEntity(requestDTO, user, crop, area);

        // 2) 여러 이미지 추가(추후에 작성)
        if(imageFiles != null && !imageFiles.isEmpty()) {
            for(MultipartFile imageFile : imageFiles) {
                if (imageFile != null && !imageFile.isEmpty()) {
                    String imageKey = "estimate/" + UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                    String imageUrl = amazonS3Manager.uploadFile(imageKey, imageFile);

                    EstimateImage estimateImage = EstimateImage.builder()
                            .estimate(estimate)
                            .imageUrl(imageUrl)
                            .build();
                    estimate.addEstimateImage(estimateImage); //Estimate 엔터티에 이미지 추가
                }
            }
        }
        // 3) 견적서 저장
        Estimate savedEstimate = estimateRepository.save(estimate);

        // 4) Response 생성
        return estimateConverter.toCreateResponseDTO(savedEstimate);
    }
    /**
     * Update(견적서 수정)
     */
    @Override
    public EstimateResponseDTO.UpdateDTO updateEstimate(
            Long estimateId, EstimateRequestDTO.UpdateDTO requestDTO) {

        // 1) 견적서 조회
        Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(() -> new IllegalArgumentException("해당 estimate id와 일치하는 견적서가 존재하지 않습니다."));

        // 2) 변경 (실제 수정 필드만 변경)
        if (requestDTO.getCategory() != null) estimate.setCategory(requestDTO.getCategory());

        if (requestDTO.getAreaName() != null) estimate.setArea(Area.builder().build());

        if (requestDTO.getBudget() != null) estimate.setBudget(requestDTO.getBudget());

        if (requestDTO.getBody() != null) estimate.setBody(requestDTO.getBody());

        if (requestDTO.getCropName() != null) estimate.setCrop(Crop.builder().build());

        // 3) 수정사항 저장
        estimateRepository.save(estimate);

        return estimateConverter.toUpdateResponseDTO(estimateId, estimate);
    }
    /**
     * Delete(견적서 삭제)
     */
    @Override
    public EstimateResponseDTO.DeleteDTO deleteEstimate(Long estimateId) {

        // 1) 견적서 조회
        Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(() -> new IllegalArgumentException("해당 estimate id와 일치하는 견적서가 존재하지 않습니다."));

        // S3에서 이미지 삭제
        List<String> imageUrls = estimate.getEstimateImageList().stream()
                .map(EstimateImage::getImageUrl)
                        .collect(Collectors.toList());

        for (String imageUrl : imageUrls) {
            //S3에서 이미지 삭제(imageUrl 에서 S3 key 추출)
            String s3key = extractS3KeyFromUrl(imageUrl);
            amazonS3Manager.deleteFile(s3key);
        }
        // 2) 견적서 삭제
        estimateRepository.delete(estimate);

        // 3) 결과 dto반환
        return estimateConverter.toDeleteResponseDTO(estimateId);
    }


}
