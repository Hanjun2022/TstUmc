package com.backend.farmon.service.ExpertService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.*;
import com.backend.farmon.config.security.UserAuthorizationUtil;
import com.backend.farmon.converter.ExpertConverter;
import com.backend.farmon.converter.SignupConverter;
import com.backend.farmon.domain.*;
import com.backend.farmon.domain.enums.Role;
import com.backend.farmon.dto.expert.ExpertCareerRequest;
import com.backend.farmon.dto.expert.ExpertProfileRequest;
import com.backend.farmon.dto.user.SignupRequest;
import com.backend.farmon.dto.user.SignupResponse;
import com.backend.farmon.repository.AreaRepository.AreaRepository;
import com.backend.farmon.repository.CropRepository.CropRepository;
import com.backend.farmon.repository.ExpertCareerRepository.ExpertCareerRepository;
import com.backend.farmon.repository.ExpertReposiotry.ExpertRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpertCommandServiceImpl implements ExpertCommandService {

    private final UserRepository userRepository;
    private final ExpertRepository expertRepository;
    private final AreaRepository areaRepository;
    private final CropRepository cropRepository;
    private final ExpertCareerRepository expertCareerRepository;
    private final UserAuthorizationUtil userAuthorizationUtil;

    // 전문가 등록 로직
    @Override
    @Transactional
    public SignupResponse.ExpertJoinResultDTO joinExpert(Long userId, SignupRequest.ExpertJoinDto request) {
        // 입력받은 유저 아이디 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        // 토큰 검증(전문가 전환을 하려는 유저는 현재 role이 농업인이어야 함)
        userAuthorizationUtil.isCurrentUserMatching(userId, Role.FARMER.toString());

        // 이미 저 유저 아이디로 등록된 전문가 엔티티가 있으면 에러
        if (expertRepository.existsByUserId(userId)) {
            throw new ExpertHandler(ErrorStatus.EXPERT_ALREADY_EXISTS);
        }

        // Expert 엔티티 생성 및 저장
        Area area = areaRepository.findByAreaNameDetail(request.getExpertLocation())
                .orElseThrow(() -> new AreaHandler(ErrorStatus.AREA_NOT_FOUND));

        Crop crop = cropRepository.findByName(request.getExpertCrop())
                .orElseThrow(() -> new CropHandler(ErrorStatus.CROP_NOT_FOUND));

        Expert newExpert = Expert.builder()
            .build();
        newExpert.setCrop(crop);
        newExpert.setArea(area);
        newExpert.setUser(user);
        expertRepository.save(newExpert);

        // 전문가 회원가입 응답 DTO생성
        return SignupConverter.toExpertJoinResultDTO(user.getId(), newExpert.getId());
    }

    // 전문가 경력 등록 로직
    @Override
    @Transactional
    public ExpertCareer postExpertCareer(Long expertId, ExpertCareerRequest.ExpertCareerPostDTO request) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new ExpertHandler(ErrorStatus.EXPERT_NOT_FOUND));

        ExpertCareer newExpertCareer = ExpertConverter.toExpertCareer(request);
        newExpertCareer.setExpert(expert);

        return expertCareerRepository.save(newExpertCareer); // 완성된 엔티티 반환
    }

    // 전문가 대표서비스 변경 로직
    @Override
    @Transactional
    public Expert updateExpertSpecialty(Long expertId, ExpertProfileRequest.UpdateSpecialtyDTO request) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new ExpertHandler(ErrorStatus.EXPERT_NOT_FOUND));

        // 입력받은 카테고리가 있고 기존 것과 다르면 업데이트 수행
        if (request.getCrop() != null && !expert.getCrop().getName().equals(request.getCrop())) {
            Crop newCrop = cropRepository.findByName(request.getCrop())
                    .orElseThrow(() -> new CropHandler(ErrorStatus.CROP_NOT_FOUND));
            expert.setCrop(newCrop);
        }
        if (request.getServiceDetail1() != null) {expert.setServiceDetail1(request.getServiceDetail1());}
        if (request.getServiceDetail2() != null) {expert.setServiceDetail2(request.getServiceDetail2());}
        if (request.getServiceDetail3() != null) {expert.setServiceDetail3(request.getServiceDetail3());}
        if (request.getServiceDetail4() != null) {expert.setServiceDetail4(request.getServiceDetail4());}

        return expertRepository.save(expert);
    }

    // 전문가 활동지역 변경 로직
    @Override
    @Transactional
    public Expert updateExpertArea(Long expertId, ExpertProfileRequest.UpdateAreaDTO request) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new ExpertHandler(ErrorStatus.EXPERT_NOT_FOUND));

        // 입력받은 카테고리가 있고 기존 것과 다르면 업데이트 수행
        if (request.getAreaNameDetail() != null && !expert.getArea().getAreaNameDetail().equals(request.getAreaNameDetail())) {
            Area newArea = areaRepository.findByAreaNameDetail(request.getAreaNameDetail())
                    .orElseThrow(() -> new AreaHandler(ErrorStatus.AREA_NOT_FOUND));
            expert.setArea(newArea);
        }
        if (request.getAvailableRange() != null) {expert.setAvailableRange(request.getAvailableRange());}
        if (request.getIsAvailableEverywhere() != null) {expert.setIsAvailableEverywhere(request.getIsAvailableEverywhere());}
        if (request.getIsExcludeIsland() != null) {expert.setIsExcludeIsland(request.getIsExcludeIsland());}

        return expertRepository.save(expert);
    }
}