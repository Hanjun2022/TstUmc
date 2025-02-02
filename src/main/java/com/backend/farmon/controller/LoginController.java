package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.config.security.UserAuthorizationUtil;
import com.backend.farmon.dto.user.LoginRequestDTO;
import com.backend.farmon.dto.user.LoginResponseDTO;
import com.backend.farmon.service.UserService.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "로그인", description = "로그인에 관한 API.")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final UserAuthorizationUtil userAuthorizationUtil;

    @PostMapping("/api/login")
    @Operation(summary = "공동로그인 API", description = " userId와 토큰을 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    public ApiResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO resultDTO = loginService.attemptLogin(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        return ApiResponse.onSuccess(resultDTO);
    }
}