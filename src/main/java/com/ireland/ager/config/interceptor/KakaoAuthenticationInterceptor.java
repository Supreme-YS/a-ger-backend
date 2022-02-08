package com.ireland.ager.config.interceptor;

import com.ireland.ager.account.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class KakaoAuthenticationInterceptor implements HandlerInterceptor {
    private final AuthServiceImpl authService;
    private static final String[] excludeList= {
            "/api/account/login-url"
            ,"/api/account/login"
            ,"/api/token/**"
            ,"/api/review/list/**"
            ,"/api/product/search"
            ,"/api/board/search"
            ,"/favicon.ico/**"
            ,"/favicon.ico"
    };
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String requestUrl = request.getRequestURI();
        log.info("URL:{}",requestUrl);
        if(!PatternMatchUtils.simpleMatch(excludeList,requestUrl)) {
            authService.isValidToken(token);
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
