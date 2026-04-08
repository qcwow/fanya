package com.example.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "学习节奏调整响应数据")
public class ProgressAdjustVO {

    @Schema(description = "调整计划")
    private AdjustPlan adjustPlan;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "调整计划详情")
    public static class AdjustPlan {

        @Schema(description = "续讲章节ID（定位原讲解节点）", example = "sec002")
        private String continueSectionId;

        @Schema(description = "调整类型：supplement（补充讲解）、accelerate（加速）、normal（正常）", example = "supplement")
        private String adjustType;

        @Schema(description = "补充讲解内容（理解程度为partial时返回）")
        private SupplementContent supplementContent;

        @Schema(description = "后续章节调整建议")
        private List<NextSection> nextSections;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "补充讲解内容")
    public static class SupplementContent {

        @Schema(description = "补充讲解文本内容", example = "为了进一步理解平面假设的简化作用，我们以矩形截面梁为例...")
        private String content;

        @Schema(description = "预计时长（秒）", example = "30")
        private Integer duration;

        @Schema(description = "相关示例", example = "工程中常见的简支梁弯曲问题，均基于平面假设推导正应力公式")
        private String relatedExample;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "后续章节调整建议")
    public static class NextSection {

        @Schema(description = "章节ID", example = "sec002")
        private String sectionId;

        @Schema(description = "调整后时长（秒）", example = "75")
        private Integer adjustedDuration;

        @Schema(description = "是否强化重点讲解", example = "true")
        private Boolean isKeyPointStrengthen;
    }
}
