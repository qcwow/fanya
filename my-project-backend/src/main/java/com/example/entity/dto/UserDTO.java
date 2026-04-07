package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "基础用户实体DTO", description = "存储系统所有用户的通用基础信息")
public class UserDTO {
    /**
     *ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "ID（自增主键）")
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
}
