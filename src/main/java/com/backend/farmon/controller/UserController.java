package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.config.security.JWTUtil;
import com.backend.farmon.domain.enums.Role;
import com.backend.farmon.dto.user.ExchangeResponse;
import com.backend.farmon.dto.user.MypageRequest;
import com.backend.farmon.dto.user.MypageResponse;
import com.backend.farmon.service.UserService.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "사용자 정보")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserQueryService userQueryService;
    private final JWTUtil jwtUtil;

    @GetMapping("/api/user/{id}")
    @Operation(summary = "마이페이지 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    public ApiResponse<MypageResponse.UserInfoDTO> getUserInfo(@PathVariable Long id){

        return null;
    }

    @PatchMapping("/api/user/{id}")
    @Operation(summary = "마이페이지 수정 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "BAD REQUEST, 잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON404", description = "NOT FOUND, 리소스를 찾을 수 없음")
    })
    public ApiResponse<MypageResponse> updateUserInfo(
            @PathVariable Long id,
            @RequestBody MypageRequest.UpdateUserInfoDTO updateUserInfoDTO
    ) {
        return null;
    }

    @GetMapping("/api/user/exchange")
    @Operation(
            summary = "사용자 역할 전환 API",
            description = "농업인 전환과 전문가 전환에 관한 API 입니다. 농업인일 경우 전문가로 전환, 전문가일 경우 농업인으로 전환되어 새로운 토큰을 발급합니다. "
                    + "요청 성공 시 새로 발급된 토큰을 요청 헤더에 넣어주세요."
                    + "유저 아이디, 사용자 유형을 쿼리 스트링으로 입력해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "EXPERT4002", description = "전문가로 등록되어 있지 않은 농업인 입니다. (농업인 -> 전문가 전환 시 전문가로 등록되어 있지 않은 사용자일 때)", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "전환하려는 역할과 현재 로그인한 역할이 일치합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "JWT 토큰이 요청 헤더에 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1"),
            @Parameter(name = "role", description = "전환하고자 하는 사용자 유형(ADMIN 제외), 전문가 전환 시 EXPERT, 농업인 전환 시 FARMER", example = "EXPERT")
    })
    public ApiResponse<ExchangeResponse> getExchangeRole(@RequestParam(name="userId") Long userId,
                                                         @RequestParam(name="role", defaultValue = "EXPERT") Role role,
                                                         HttpServletRequest request) {
        // JWTUtil을 통해 토큰 추출
        String token = jwtUtil.extractTokenFromRequest(request);
        ExchangeResponse response = userQueryService.exchangeRole(userId, role, token);

        return ApiResponse.onSuccess(response);
    }
}
