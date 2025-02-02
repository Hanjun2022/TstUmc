package com.backend.farmon.service.UserService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.UserHandler;
import com.backend.farmon.config.security.JWTUtil;
import com.backend.farmon.domain.User;
import com.backend.farmon.domain.enums.Role;
import com.backend.farmon.dto.user.LoginResponseDTO;
import com.backend.farmon.repository.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // 로그인 요청 (이메일, 비밀번호 확인 후 JWT 생성)
    @Transactional
    public LoginResponseDTO attemptLogin(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        user.setRole(Role.FARMER);// 최초 로그인시 role 농업인으로 초기화

        // authenticationManager에서 사용자 인증 후 authentication객체에 저장
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        // SecurityContext에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 앞으로 로그인시 사용될 jwt token 생성
        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().toString());

        // 응답 DTO 생성
        return LoginResponseDTO.builder()
                .userId(user.getId())
                .role(user.getRole())
                .email(user.getEmail())
                .userName(user.getUserName())
                .token(token)
                .build();
    }
}