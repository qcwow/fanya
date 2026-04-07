package com.example.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "用户同步响应结果", description = "用户信息同步成功后，返回系统内部生成的唯一用户标识及用于后续访问的身份验证令牌")
public class SyncUserVO {

    @Schema(description = "系统内部生成的唯一用户ID（对应平台用户在本系统中的唯一标识）", example = "stu20001")
    private String internalUserId;

    @Schema(description = "同步处理状态标识（success 表示处理成功）", example = "success")
    private String syncStatus;

    @Schema(description = "身份验证令牌（JWT），同步成功后下发，用于后续业务接口调用的身份核验与权限识别",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJzdHUyMDAwMSJ9.xxx...")
    private String authToken;
}