package com.backend.farmon.service.UserService;

import com.backend.farmon.domain.User;
import com.backend.farmon.dto.user.SignupRequest;

public interface UserCommandService {
    User joinUser(SignupRequest.UserJoinDto request);
}
