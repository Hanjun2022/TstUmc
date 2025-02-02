package com.backend.farmon.validaton.annotation;

import java.lang.annotation.*;

@Documented // 사용자 정의 어노테이션
@Target({ElementType.PARAMETER}) // 어노테이션 적용 범위
@Retention(RetentionPolicy.RUNTIME) // 실행중에만
public @interface CheckPage {

}
