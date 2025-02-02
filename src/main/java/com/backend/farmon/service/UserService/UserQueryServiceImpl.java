package com.backend.farmon.service.UserService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.ExpertHandler;
import com.backend.farmon.apiPayload.exception.handler.UserHandler;
import com.backend.farmon.config.security.JWTUtil;
import com.backend.farmon.config.security.UserAuthorizationUtil;
import com.backend.farmon.converter.UserConverter;
import com.backend.farmon.domain.Expert;
import com.backend.farmon.domain.User;
import com.backend.farmon.domain.enums.Role;
import com.backend.farmon.dto.user.ExchangeResponse;
import com.backend.farmon.repository.ExpertReposiotry.ExpertRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;
    private final ExpertRepository expertRepository;
    private final JWTUtil jwtUtil;
    private final UserAuthorizationUtil userAuthorizationUtill;

    @Override
    public ExchangeResponse exchangeRole(Long userId, Role role, String existingToken) {
        // 역할 전환 시 현재 역할과 중복되지 않은지 검사
        if(userAuthorizationUtill.isCurrentUserRoleMatching(role.toString()))
            throw new UserHandler(ErrorStatus.EXCHANGE_ROLE_SAME);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 농업인 -> 전문가 전환 시 전문가로 등록되어 있는 지 확인
        Expert expert = (role.equals(Role.EXPERT))
                ? expertRepository.findExpertByUserId(userId)
                .orElseThrow(() -> new ExpertHandler(ErrorStatus.EXPERT_NOT_REGISTER))
                : null;

        // 농업인 - 전문가 전환을 위해 역할이 변경된 새로운 토큰을 생성
        String newToken = jwtUtil.updateRoleInToken(existingToken, role.toString());

        // 토큰에서 새 역할 추출
        String updatedRole = jwtUtil.extractRole(newToken);

        // 인증정보 업데이트
        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
                jwtUtil.extractUserId(newToken), // User ID
                null,                            // 자격증명 (패스워드 등)
                Collections.singletonList(new SimpleGrantedAuthority(updatedRole)) // 새 역할
        );

        // SecurityContext에 새 인증정보 설정
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);

        log.info("농업인 - 전문가 전환 새 토큰 발급 완료");
        log.info("새로 전환된 역할: {}, userId: {}",
                userAuthorizationUtill.getCurrentUserRole(), userAuthorizationUtill.getCurrentUserId());

        return UserConverter.toExchangeResponse(userId, updatedRole, expert, newToken);
    }
}
