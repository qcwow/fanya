package com.example.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "课程信息同步请求参数", description = "封装外部教育平台同步课程时所需的业务参数、课程详细信息及安全校验码")
public class SyncCourseDTO {

    @NotBlank(message = "platformId不能为空")
    @Schema(description = "外部平台唯一标识", example = "plat001")
    private String platformId;

    @NotNull(message = "courseInfo不能为空")
    @Schema(description = "详细课程信息对象")
    private CourseInfoDTO courseInfo;

    @Schema(description = "签名时间戳，格式为yyyy-MM-ddHH:mm:ss", example = "2024-05-2011:00:00")
    private String time;

    @NotBlank(message = "enc不能为空")
    @Schema(description = "加密校验码/签名串（用于接口安全与身份校验）", example = "435FCD6C41E421E43DF2C1FC3888F12D")
    private String enc;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "课程详细信息内容")
    public static class CourseInfoDTO {
        @Schema(description = "外部平台课程唯一标识", example = "plat_cou001")
        private String courseId;

        @Schema(description = "课程名称", example = "材料力学（上册）")
        private String courseName;

        @Schema(description = "所属学校ID", example = "sch10001")
        private String schoolId;

        @Schema(description = "所属学校名称", example = "某某大学")
        private String schoolName;

        @Schema(description = "授课教师信息列表")
        private List<TeacherInfo> teacherInfo;

        @Schema(description = "开课学期标识", example = "20242")
        private String term;

        @Schema(description = "课程学分", example = "3.0")
        private Double credit;

        @Schema(description = "课程总学时", example = "48")
        private Integer period;

        @Schema(description = "课程封面图片访问地址", example = "http://xxx.com/course/cover/001.jpg")
        private String courseCover;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "教师基础信息")
    public static class TeacherInfo {
        @Schema(description = "教师工号/平台唯一ID", example = "plat_tea001")
        private String teacherId;

        @Schema(description = "教师真实姓名", example = "张教授")
        private String teacherName;
    }
}