package com.backend.farmon.service.UserService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.UserHandler;
import com.backend.farmon.converter.SignupConverter;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.user.SignupRequest;
import com.backend.farmon.repository.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User joinUser(SignupRequest.UserJoinDto request) {
        Boolean emailExists = userRepository.existsByEmail(request.getEmail());
        if(emailExists) {
            throw new UserHandler(ErrorStatus.EMAIL_ALREADY_EXIST);
        }

        User newUser = SignupConverter.toUser(request);
        newUser.encodePassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(newUser);
    }
}