package com.example.filter;

import com.alibaba.fastjson2.JSONObject;
import com.example.utils.Const;
import com.example.utils.SnowflakeIdGenerator;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Set;

/**
 * 请求日志过滤器，用于记录所有用户请求信息
 */
@Slf4j
@Component
public class RequestLogFilter extends OncePerRequestFilter {

    @Resource
    SnowflakeIdGenerator generator;

    // 完善后的忽略列表
    private final Set<String> ignores = Set.of(
            "/swagger-ui",
            "/v3/api-docs",
            "/doc.html",
            "/webjars",
            "/favicon.ico",
            "/error"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();

        // 判定是否是静态资源或忽略的URL
        if(this.isIgnoreUrl(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        long startTime = System.currentTimeMillis();
        this.logRequestStart(request);

        // 使用包装类缓存响应内容
        ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper(response);
        try {
            filterChain.doFilter(request, wrapper);
            this.logRequestEnd(wrapper, startTime);
        } finally {
            // 必须执行此行，否则前端收不到数据
            wrapper.copyBodyToResponse();
        }
    }

    /**
     * 判定当前请求url是否不需要详细日志
     */
    private boolean isIgnoreUrl(String url){
        // 1. 检查前缀列表
        for (String ignore : ignores) {
            if(url.startsWith(ignore)) return true;
        }
        // 2. 检查静态文件后缀
        return url.endsWith(".js") || url.endsWith(".css") ||
                url.endsWith(".png") || url.endsWith(".jpg") || url.endsWith(".html");
    }

    /**
     * 请求结束时的日志打印
     */
    public void logRequestEnd(ContentCachingResponseWrapper wrapper, long startTime){
        long time = System.currentTimeMillis() - startTime;
        int status = wrapper.getStatus();
        String contentType = wrapper.getContentType();
        String content;

        // 核心改进：只在返回 JSON 且状态为 200 时打印 Body
        if (status == 200 && contentType != null && contentType.contains("application/json")) {
            byte[] body = wrapper.getContentAsByteArray();
            content = new String(body);
            // 如果 JSON 实在太长，可以截断打印
            if (content.length() > 1000) {
                content = content.substring(0, 1000) + "... (内容过长已截断)";
            }
        } else {
            // 非 JSON 数据（如网页、图片、错误页面）只记录状态
            content = "[非JSON或非成功响应，跳过正文打印] Content-Type: " + contentType;
        }

        log.info("请求处理耗时: {}ms | 响应状态: {} | 响应结果: {}", time, status, content);
    }

    /**
     * 请求开始时的日志打印
     */
    public void logRequestStart(HttpServletRequest request){
        long reqId = generator.nextId();
        MDC.put("reqId", String.valueOf(reqId));

        JSONObject object = new JSONObject();
        request.getParameterMap().forEach((k, v) -> object.put(k, v.length > 0 ? v[0] : null));

        Object id = request.getAttribute(Const.ATTR_USER_ID);
        if(id != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof User user) {
                log.info("请求URL: \"{}\" ({}) | 远程IP: {} │ 用户: {} (ID: {}) | 权限: {} | 参数: {}",
                        request.getServletPath(), request.getMethod(), request.getRemoteAddr(),
                        user.getUsername(), id, user.getAuthorities(), object);
                return;
            }
        }
        log.info("请求URL: \"{}\" ({}) | 远程IP: {} │ 身份: 未验证 | 参数: {}",
                request.getServletPath(), request.getMethod(), request.getRemoteAddr(), object);
    }
}