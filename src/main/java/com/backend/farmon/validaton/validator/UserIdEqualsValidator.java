package com.backend.farmon.validaton.validator;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.AuthorizationHandler;
import com.backend.farmon.config.security.JWTUtil;
import com.backend.farmon.config.security.UserAuthorizationUtil;
import com.backend.farmon.validaton.annotation.EqualsUserId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


// 토큰에 설정되어 있는 userId와 파라미터로 받은 userId가 같은 지 검증
//@Order(Ordered.HIGHEST_PRECEDENCE + 50) // 우선순위 올리기
@Component
@RequiredArgsConstructor
public class UserIdEqualsValidator implements ConstraintValidator<EqualsUserId, Long> {
    private final UserAuthorizationUtil userAuthorizationUtil;

    @Override
    public void initialize(EqualsUserId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext context) {
        if (userId == null) {
            return false; // null일 경우 false 반환
        }

        boolean isValid;
        try {
            isValid = userAuthorizationUtil.isCurrentUserIdMatching(userId);
        } catch (AuthorizationHandler ex) {
            // Hibernate Validator 내부에서는 예외를 던지지 않고 false 반환
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus._UNAUTHORIZED.toString()).addConstraintViolation();
            return false;
        } catch (Exception ex) {
            // 다른 예외 발생 시도 마찬가지로 처리
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus._INTERNAL_SERVER_ERROR.toString()).addConstraintViolation();
            return false;
        }

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.AUTHORIZATION_NOT_EQUALS.toString()).addConstraintViolation();
        }

        return isValid;
    }

}
