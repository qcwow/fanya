package com.example.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "进度追踪请求参数", description = "用于记录和同步用户在课件学习中的实时进度、停留位置及最后操作时间")
public class ProgressTrackDTO {

    @Schema(description = "学校ID", example = "SCH_001")
    private String schoolId;

    @Schema(description = "用户ID", example = "10086")
    private String userId;

    @Schema(description = "课程ID", example = "5001")
    private String courseId;

    @Schema(description = "课件/资源ID", example = "2001")
    private String lessonId;

    @Schema(description = "当前学习的小节或知识切片ID", example = "12")
    private String currentSectionId;

    @Schema(description = "学习进度百分比（0.00-100.00）", example = "85.5")
    private Float progressPercent;

    @Schema(description = "最后一次操作的时间戳或格式化时间", example = "2023-10-27 15:30:00")
    private String lastOperateTime;

    @Schema(description = "关联的问答记录ID（如果进度更新是由问答触发的，则记录该ID）", example = "9527")
    private String qaRecordId;

    @Schema(description = "加密校验码/签名串（用于接口数据完整性校验）")
    private String enc;
}