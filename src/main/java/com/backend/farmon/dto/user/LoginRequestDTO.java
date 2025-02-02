package com.backend.farmon.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
public class LoginRequestDTO {
    @Schema(description = "이메일 주소", example = "umc@gmail.com")
    private String email;

    @Schema(description = "비밀번호", example = "qwer1234")
    private String password;
}
