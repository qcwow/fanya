package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * API 签名配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "api.security")
public class ApiSecurityConfig {
    
    /**
     * 签名密钥（生产环境应从环境变量或配置中心读取）
     */
    private String staticKey = "your_static_key_here";
    
    /**
     * 签名有效期（秒），默认 5 分钟
     */
    private long signatureExpireSeconds = 300;
}
