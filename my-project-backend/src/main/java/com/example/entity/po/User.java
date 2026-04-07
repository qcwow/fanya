package com.example.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "基础用户实体", description = "存储系统所有用户的通用基础信息")
@TableName("db_user")
public class User {
    /**
     *ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "ID（自增主键）")
    private Long id;
    /**
     * 外部平台ID
     */
    @Schema(description = "外部平台标识（如: plat001）")
    private String platformId; // 新增：必须有，区分不同来源的平台
    /**
     * 外部平台用户id
     */
    @Schema(description = "外部平台用户ID")
    private String platformUserId;
    /**
     * 用户名
     */
    @Schema(description = "登录用户名（唯一）")
    private String username;
    /**
     * 密码
     */
    @Schema(description = "登录密码（加密存储）")
    private String password;
    /**
     * 用户类型
     */
    @Schema(description = "用户类型（1学生，2老师）")
    private Integer role;
    /**
     * 学校id
     */
    @Schema(description = "学校id")
    private String schoolId;
    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名")
    private String realName;
    /**
     * 联系电话
     */
    @Schema(description = "联系电话（对应同步接口中的 contactInfo.phone）")
    private String phone;
    /**
     * 电子邮箱
     */
    @Schema(description = "电子邮箱（对应同步接口中的 contactInfo.email）")
    private String email;
    /**
     * 注册同步时间
     */
    @Schema(description = "注册/同步时间")
    private LocalDateTime createTime;
}