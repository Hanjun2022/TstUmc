package com.backend.farmon.validaton.validator;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.PageHandler;
import com.backend.farmon.validaton.annotation.CheckPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 99) // 우선순위 올리기
@Component
public class PageCheckValidator implements HandlerMethodArgumentResolver {

    // resolver 실행 여부 검증
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // @CheckPage와 Integer 타입 파라미터만 처리
        boolean hasCheckPageAnnotation = parameter.hasParameterAnnotation(CheckPage.class);
        boolean isIntegerType = parameter.getParameterType().equals(Integer.class);

        log.info("supportsParameter 호출 - @CheckPage: {}, 타입: {}",
                hasCheckPageAnnotation, isIntegerType);

        return hasCheckPageAnnotation && isIntegerType;
    }

    // 새로운 값 바인딩
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String pageParam = webRequest.getParameter("page");
        log.info("받은 page 파라미터: " + pageParam);

        if (pageParam == null || pageParam.isEmpty()) {
            throw new PageHandler(ErrorStatus.PAGE_NOT_FOUND); // null 또는 비어있는 경우 에러
        }

        int page = Integer.parseInt(pageParam);
        if (page < 1) {
            throw new PageHandler(ErrorStatus.PAGE_NOT_FOUND); // 1보다 작은 경우 에러
        }

        return page - 1;
    }
}
