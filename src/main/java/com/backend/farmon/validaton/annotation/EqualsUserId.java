package com.backend.farmon.validaton.annotation;

import com.backend.farmon.validaton.validator.UserIdEqualsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented // 사용자 정의 어노테이션
@Constraint(validatedBy = UserIdEqualsValidator.class) // 검증 로직을 구현한 클래스 지정
@Target({ElementType.PARAMETER, ElementType.FIELD}) // 어노테이션 적용 범위
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지
public @interface EqualsUserId { // 토큰에 설정되어 있는 userId와 파라미터로 받은 userId가 같은 지 검증
    String message() default "인증된 사용자 정보와 요청된 리소스의 사용자 정보가 다릅니다."; // 기본 에러 메시지

    Class<?>[] groups() default {}; // 유효성 검사 그룹

    Class<? extends Payload>[] payload() default {}; // 메타데이터 전달용
}
