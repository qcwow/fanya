package com.example.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "学习进度追踪请求参数", description = "记录学生学习进度、问答交互记录，为适配调整提供数据支撑")
public class ProgressTrackDTO {

    @Schema(description = "学校ID", example = "sch10001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String schoolId;

    @Schema(description = "学生学号/用户ID", example = "stu20001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    @Schema(description = "课程ID", example = "cou30001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String courseId;

    @Schema(description = "智课ID", example = "lesson20240520001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lessonId;

    @Schema(description = "当前学习章节ID", example = "sec002", requiredMode = Schema.RequiredMode.REQUIRED)
    private String currentSectionId;

    @Schema(description = "章节学习进度（0-100）", example = "60.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Float progressPercent;

    @Schema(description = "最后操作时间", example = "2024-05-20 10:10:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastOperateTime;

    @Schema(description = "最近问答记录ID（如有）", example = "ans20240520001")
    private String qaRecordId;

    @Schema(description = "当前时间（用于签名验证），格式：yyyy-MM-dd HH:mm:ss", example = "2024-05-20 10:10:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private String time;

    @Schema(description = "签名信息", example = "B5C7D9E1F3A5B7C9D1E3F5A7B9C1D3E5", requiredMode = Schema.RequiredMode.REQUIRED)
    private String enc;
}