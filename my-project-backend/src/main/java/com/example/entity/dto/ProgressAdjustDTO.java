package com.example.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "学习节奏调整请求参数", description = "基于学生理解程度与学习进度，调整后续讲授节奏")
public class ProgressAdjustDTO {

    @Schema(description = "学生学号/用户ID", example = "stu20001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    @Schema(description = "智课ID", example = "lesson20240520001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lessonId;

    @Schema(description = "当前章节ID", example = "sec002", requiredMode = Schema.RequiredMode.REQUIRED)
    private String currentSectionId;

    @Schema(description = "理解程度（来自问答结果）：none/partial/full", example = "partial", requiredMode = Schema.RequiredMode.REQUIRED)
    private String understandingLevel;

    @Schema(description = "问答记录ID", example = "ans20240520001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String qaRecordId;

    @Schema(description = "当前时间（用于签名验证），格式：yyyy-MM-dd HH:mm:ss", example = "2024-05-20 10:10:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private String time;

    @Schema(description = "签名信息", example = "C7D9E1F3A5B7C9D1E3F5A7B9C1D3E5F7", requiredMode = Schema.RequiredMode.REQUIRED)
    private String enc;
}
