package com.backend.farmon.dto.user;

import com.backend.farmon.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class SignupRequest {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "농업인(일반 회원) 회원가입 요청 DTO")
    public static class UserJoinDto{
        @Schema(description = "이름", example = "홍길동")
        @NotBlank
        String name;

        @Schema(description = "생년월일", example = "2025-01-01")
        @NotNull
        LocalDate birth;

        @Schema(description = "성별 (MALE,FEMALE) ", example = "MALE")
        @NotNull
        Gender gender;

        @Schema(description = "이메일 주소", example = "umc@gmail.com")
        @NotBlank
        @Email
        String email;

        @Schema(description = "비밀번호", example = "qwer1234")
        @NotBlank
        String password;

        @Schema(description = "휴대전화번호", example = "01012345678")
        @NotBlank
        String phone;
    }


    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "전문가 회원가입 요청 DTO")
    public static class ExpertJoinDto{
        @Schema(description = "전문가 전문 분야 카테고리(세부 카테고리로 전달 ex.쌀, 옥수수)")
        String expertCrop;

        @Schema(description = "전문가 활동 위치 카테고리(세부 카테고리로 전달 ex.강남구, 남양주시)")
        String expertLocation;
    }
}
