package com.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger开发文档相关配置
 */
@Configuration
// 保留安全校验配置，这样Knife4j右上角会出现“Authorize”按钮，用于填入Token
@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "Bearer",
        name = "Authorization", in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(security = { @SecurityRequirement(name = "Authorization") })
public class SwaggerConfiguration {

    /**
     * 配置文档介绍以及详细信息
     * @return OpenAPI
     */
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("泛雅平台 AI 互动智课生成系统")
                        .description("本文档基于超星集团开放 API 设计规范，用于智课生成、实时问答及进度适配等核心业务调试。")
                        .version("1.0"));
    }

    /*
       这里删除了 customerGlobalHeaderOpenApiCustomizer 和 authorizePathItems
       因为我们不再需要手动硬编码添加接口，Swagger 会自动扫描 UserController
    */
}