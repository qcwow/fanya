package com.example.config;

import com.example.entity.RestBean;
import com.example.filter.JwtAuthenticationFilter;
import com.example.filter.RequestLogFilter;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfiguration {

    @Resource
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Resource
    RequestLogFilter requestLogFilter;

    @Resource
    JwtUtils utils;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        // 允许登录接口、Swagger和错误页面访问
                        .requestMatchers("/api/v1/user/login", "/error").permitAll()
                        // 允许智课模块接口（开发测试用，生产环境应移除）
                        .requestMatchers("/api/v1/lesson/**").permitAll()
                        // 允许学习进度模块接口（通过签名验证保证安全）
                        .requestMatchers("/api/v1/progress/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/doc.html"
                        ).permitAll()
                        // 其他所有请求都需要登录后才能访问
                        .anyRequest().authenticated()
                )
                // 禁用 CSRF（因为是无状态 JWT）
                .csrf(AbstractHttpConfigurer::disable)
                // 开启无状态 Session
                .sessionManagement(conf -> conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 异常处理逻辑
                .exceptionHandling(conf -> conf
                        .accessDeniedHandler(this::handleAccessDenied) // 403
                        .authenticationEntryPoint(this::handleUnauthorized) // 401
                )
                // 过滤器顺序
                .addFilterBefore(requestLogFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, RequestLogFilter.class)
                .build();
    }

    /**
     * 403 无权限处理逻辑
     */
    private void handleAccessDenied(HttpServletRequest request,
                                    HttpServletResponse response,
                                    AccessDeniedException exception) throws IOException {
        this.renderError(response, RestBean.forbidden(exception.getMessage()));
    }

    /**
     * 401 未登录处理逻辑
     */
    private void handleUnauthorized(HttpServletRequest request,
                                    HttpServletResponse response,
                                    AuthenticationException exception) throws IOException {
        this.renderError(response, RestBean.unauthorized(exception.getMessage()));
    }

    /**
     * 统一渲染 JSON 错误响应
     */
    private void renderError(HttpServletResponse response, RestBean<?> bean) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(bean.asJsonString());
    }
}