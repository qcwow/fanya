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

    @Schema(description = "学校ID", example = "SCH_001")
    private String schoolId;

    @Schema(description = "用户ID（上传者）", example = "10086")
    private String userId;

    @Schema(description = "课程ID", example = "5001")
    private String courseId;

    @Schema(description = "文件类型（如：pdf, pptx, docx）", example = "pdf")
    private String fileType;

    @Schema(description = "文件下载或访问的URL地址", example = "https://oss.example.com/files/lesson01.pdf")
    private String fileUrl;

    @Schema(description = "是否提取核心知识点（true: 提取, false: 仅解析文本）", example = "true")
    private Boolean isExtractKeyPoint;

    @Schema(description = "安全签名校验码（必填，用于接口防篡改）", example = "a7b8c9d0e1f2")
    private String enc;
}