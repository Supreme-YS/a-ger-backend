package com.ireland.ager.config.interceptor;

import com.ireland.ager.account.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
@RequiredArgsConstructor
public class KakaoAuthenticationInterceptor implements HandlerInterceptor {
    private final AuthServiceImpl authService;
    private static final String[] excludeList = {
            "/api/account/login-url"
            , "/api/account/login"
            , "/api/account/token/**"
            , "/api/review/list/**"
            , "/favicon.ico/**"
            , "/favicon.ico"
            , "/kafka/*"
            , "/kafka/**"
            , "/socket.io/*"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String requestUrl = request.getRequestURI();
        log.info("URL:{}", requestUrl);
        if (!PatternMatchUtils.simpleMatch(excludeList, requestUrl)) {
            log.info("Not Match");
            authService.isValidToken(token);
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
