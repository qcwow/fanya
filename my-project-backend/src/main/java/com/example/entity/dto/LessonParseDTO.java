package com.example.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "课件解析请求参数", description = "用于发起课件内容的 AI 解析任务，包含文件元数据及解析选项")
public class LessonParseDTO {

    @Schema(description = "学校ID（对接时提供）", example = "sch10001", required = true)
    private String schoolId;

    @Schema(description = "用户ID（教师工号/平台用户唯一标识）", example = "tea20001", required = true)
    private String userId;

    @Schema(description = "课程ID（关联课程体系）", example = "cou30001", required = true)
    private String courseId;

    @Schema(description = "课件文件类型", example = "pdf", required = true, allowableValues = {"ppt", "pdf"})
    private String fileType;

    @Schema(description = "课件文件存储URL（支持公网可访问地址或平台内存储路径）", example = "http://xxx.com/course/ppt/123.pdf", required = true)
    private String fileUrl;

    @Schema(description = "是否自动提取重点（默认true）", example = "true")
    private Boolean isExtractKeyPoint = true;

    @Schema(description = "当前时间（格式：yyyy-MM-dd HH:mm:ss）", example = "2024-05-20 10:30:00", required = true)
    private String time;

    @Schema(description = "签名信息（MD5加密）", example = "C4C859FB8E0E2035DB8C69212E89838A", required = true)
    private String enc;
}