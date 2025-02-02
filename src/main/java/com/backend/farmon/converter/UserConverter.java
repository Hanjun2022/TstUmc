package com.backend.farmon.converter;

import com.backend.farmon.domain.Expert;
import com.backend.farmon.domain.enums.Role;
import com.backend.farmon.dto.user.ExchangeResponse;

public class UserConverter {
    public static ExchangeResponse toExchangeResponse(Long userId, String role, Expert expert, String token) {
        ExchangeResponse.ExchangeResponseBuilder responseBuilder = ExchangeResponse.builder()
                .userId(userId)
                .exchangeRole(role)
                .token(token);

        // 전문가로 등록되어 있는 사용자라면 전문가 아이디도 반환
        if (expert != null) {
            responseBuilder.expertId(expert.getId());
        }

        return responseBuilder.build();
    }
}
