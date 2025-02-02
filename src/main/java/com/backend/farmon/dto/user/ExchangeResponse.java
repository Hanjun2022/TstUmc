package com.backend.farmon.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeResponse {

    @Schema(description = "전환에 성공한 역할, 농업인 전환일 경우 String 타입으로 FARMER, 전문가 전환일 경우 EXPERT", example = "EXPERT")
    private String exchangeRole;

    @Schema(description = "유저 아이디")
    private Long userId;

    @Schema(description = "전문가 아이디, 전문가로 등록된 사용자일 경우에만 반환")
    private Long expertId;

    @Schema(description = "역할 전환으로 인해 새로 발급된 토큰, 앞으로는 해당 토큰을 해더에 포함시키면 됩니다.")
    private String token;
}
