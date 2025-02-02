package com.backend.farmon.dto.user;

import com.backend.farmon.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
public class LoginResponseDTO {
    private Long userId;
    private Role role;
    private String email;
    private String userName;
    private String token;
}
