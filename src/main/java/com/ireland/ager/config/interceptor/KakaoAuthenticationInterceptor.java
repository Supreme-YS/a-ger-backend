package com.ireland.ager.config.interceptor;

import com.ireland.ager.account.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
@RequiredArgsConstructor
public class KakaoAuthenticationInterceptor implements HandlerInterceptor {
    private final AuthServiceImpl authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        authService.isValidToken(token);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
