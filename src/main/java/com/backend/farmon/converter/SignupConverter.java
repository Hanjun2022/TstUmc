package com.backend.farmon.converter;

import com.backend.farmon.domain.Area;
import com.backend.farmon.domain.Crop;
import com.backend.farmon.domain.Expert;
import com.backend.farmon.domain.User;
import com.backend.farmon.domain.enums.Role;
import com.backend.farmon.dto.user.SignupRequest;
import com.backend.farmon.dto.user.SignupResponse;

import java.time.LocalDateTime;

public class SignupConverter {

    // 농업인 회원가입시 엔티티 생성
    public static User toUser(SignupRequest.UserJoinDto request){
        return User.builder()
                .userName(request.getName())
                .gender(request.getGender())
                .birthDate(request.getBirth())
                .email(request.getEmail())
                .phoneNum(request.getPhone())
                // .role(request.getRole()) 기본적으로 농업인으로 설정됨
                .build();
    }

    // 농업인 회원가입시 응답 DTO 생성
    public static SignupResponse.JoinResultDTO toJoinResultDTO(User user){
        return SignupResponse.JoinResultDTO.builder()
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 전문가 회원가입시 응답 DTO 생성
    public static SignupResponse.ExpertJoinResultDTO toExpertJoinResultDTO(Long userId, Long expertId){
        return SignupResponse.ExpertJoinResultDTO.builder()
                .userId(userId)
                .expertId(expertId)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
