package com.example.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "课程同步响应结果", description = "课程基础信息同步成功后，返回系统内部生成的唯一标识及处理状态")
public class SyncCourseVO {

    @Schema(description = "系统内部生成的课程唯一ID", example = "cou30001")
    private String internalCourseId;

    @Schema(description = "同步处理状态（success表示成功）", example = "success")
    private String syncStatus;

    @Schema(description = "同步完成的系统时间，格式为yyyy-MM-dd HH:mm:ss", example = "2024-05-20 11:00:00")
    private String syncTime;
}