package com.backend.farmon.service.UserService;

import com.backend.farmon.domain.enums.Role;
import com.backend.farmon.dto.user.ExchangeResponse;

public interface UserQueryService {

    // 농업인 - 전문가 전환
    ExchangeResponse exchangeRole(Long userId, Role role, String existingToken);
}
