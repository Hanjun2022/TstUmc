package com.backend.farmon.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

public class MypageRequest {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "마이페이지 수정 요청 DTO")
    public static class UpdateUserInfoDTO{
        @Schema(description = "변경할 이메일 주소", example = "umc@gmail.com")
        String email;

        @Schema(description = "변경할 비밀번호", example = "qwer1234")
        String password;
    }


}
