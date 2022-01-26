package com.ireland.ager.config;

import com.ireland.ager.config.interceptor.KakaoAuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class KakaoAuthenticationConfig implements WebMvcConfigurer {
    private final KakaoAuthenticationInterceptor kakaoAuthenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(kakaoAuthenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/api/account/login"
                        ,"/api/token/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
