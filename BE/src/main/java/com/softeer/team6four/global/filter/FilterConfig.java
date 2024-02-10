package com.softeer.team6four.global.filter;

import com.softeer.team6four.global.auth.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {
    private final JwtProvider jwtProvider;

    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilterRegistrationBean() {
        FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoggingFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilterRegistrationBean() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter(jwtProvider));
        registrationBean.addUrlPatterns("/*"); // 필터를 적용할 URL 패턴
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
