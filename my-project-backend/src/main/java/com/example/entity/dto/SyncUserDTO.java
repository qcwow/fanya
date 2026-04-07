package com.example.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "用户信息同步请求参数", description = "用于接收外部教育平台同步的用户基础信息、角色权限、联系方式及关联课程数据")
public class SyncUserDTO {

    @Schema(description = "外部平台唯一标识", example = "plat001")
    private String platformId;

    @Schema(description = "详细用户信息对象")
    private UserInfoDTO userInfo;

    @Schema(description = "签名时间戳，格式为yyyy-MM-ddHH:mm:ss", example = "2024-05-2011:00:00")
    private String time;

    @Schema(description = "加密校验码/签名串（用于接口安全与身份校验）", example = "716766487E875DF54B9D03478985188F")
    private String enc;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "用户详细信息")
    public static class UserInfoDTO {

        @Schema(description = "外部平台用户唯一ID（工号/学号）", example = "plat_stu001")
        private String userId;

        @Schema(description = "用户真实姓名", example = "李四")
        private String userName;

        @Schema(description = "用户角色标识（student:学生, teacher:教师）", example = "student")
        private String role;

        @Schema(description = "所属学校唯一标识", example = "sch10001")
        private String schoolId;

        @Schema(description = "该用户在外部平台关联的课程ID列表")
        private List<String> relatedCourseIds;

        @Schema(description = "用户联系方式信息")
        private ContactInfoDTO contactInfo;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "用户联系方式")
    public static class ContactInfoDTO {

        @Schema(description = "联系电话", example = "13800138000")
        private String phone;

        @Schema(description = "电子邮箱地址", example = "lisi@example.com")
        private String email;
    }
}