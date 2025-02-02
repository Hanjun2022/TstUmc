package com.backend.farmon.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

public class MypageResponse {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "마이페이지 조회시 응답 DTO")
    public static class UserInfoDTO{
        String name;
        LocalDate birth;
        String gender;  // String으로 받되, 후에 Enum으로 변환
        String phone;
        String email;
        String password;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "마이페이시 수정시 응답 DTO")
    public static class UpdateUserInfoResultDTO{
        String email;
        String password;
    }

}
