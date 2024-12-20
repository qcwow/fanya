package com.example.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "用户信息展示对象")
public class UserVO {
    /**
     *ID
     */
    private Long id;
    /**
     * 用户名
     */
    @Schema(description = "登录用户名（唯一）")
    private String username;
    /**
     * 用户类型
     */
    @Schema(description = "用户类型（1学生，2老师）")
    private Integer role;
    private String token;
    @Schema(description = "用户类型（TEACHER，STUDENT")
    private String roleName;
}
