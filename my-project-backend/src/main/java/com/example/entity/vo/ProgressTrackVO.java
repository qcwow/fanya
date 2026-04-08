package com.example.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "学习进度追踪响应数据")
public class ProgressTrackVO {

    @Schema(description = "追踪记录ID", example = "track20240520001")
    private String trackId;

    @Schema(description = "智课总学习进度（0-100）", example = "45.2")
    private Float totalProgress;

    @Schema(description = "建议后续学习章节（基于问答结果适配）", example = "sec002")
    private String nextSectionSuggest;
}
