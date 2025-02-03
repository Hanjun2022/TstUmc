package com.backend.farmon.config;

import com.backend.farmon.validaton.validator.PageCheckValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final PageCheckValidator pageCheckValidator;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
                .allowedOrigins("http://localhost:5173", "http://localhost:8080", "http://43.201.137.131:8080") // 허용할 Origin 설정
                .allowedHeaders("*") // 요청을 허용할 헤더 설정
                .exposedHeaders("*")  // 응답 헤더 설정
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 허용할 HTTP 메서드 명시
                .allowCredentials(true) // 자격 증명(쿠키, 인증 헤더 등)을 포함한 요청을 허용
                .maxAge(3600); // Preflight 요청 결과를 캐시하는 시간 (초)
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(0, pageCheckValidator); // resolver 등록
    }
}