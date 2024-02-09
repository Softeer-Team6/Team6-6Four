package com.softeer.team6four.global.interceptor;

import com.softeer.team6four.global.annotation.Auth;
import com.softeer.team6four.global.auth.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (hasAuthAnnotation(handler)) {
            // JWT 유효성 검증
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                jwtProvider.validateToken(token);
            }
            return true;
        }
        return false;
    }

    /**
     * Auth 어노테이션이 있는지 확인
     * @param handler
     * @return
     */
    private boolean hasAuthAnnotation(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        return handlerMethod.getClass().getAnnotation(Auth.class) != null ||
                handlerMethod.getBeanType().getAnnotation(Auth.class) != null ||
                handlerMethod.getMethod().getAnnotation(Auth.class) != null;
    }
}
