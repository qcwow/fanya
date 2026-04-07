package com.example.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "问答响应视图对象", description = "返回 AI 生成的回答内容、关联知识点及后续学习建议")
public class QaVO {

    @Schema(description = "回答记录唯一ID / 追溯ID", example = "ANS_778899")
    private String answerId;

    @Schema(description = "AI 返回的回答正文内容（支持 Markdown 格式）",
            example = "深度学习是机器学习的一个分支，它试图模拟人脑的神经网络...")
    private String answerContent;

    @Schema(description = "回答类型（如：TEXT, CODE, IMAGE, CHART）", example = "TEXT")
    private String answerType;

    @Schema(description = "关联的知识点详情（Key-Value 结构，存储知识点名称及简单描述）")
    private Map<String, Object> relatedKnowledge;

    @Schema(description = "学习建议列表（针对当前问题的后续学习路径建议）")
    private List<String> suggestions;

    @Schema(description = "理解程度评估（基于问答上下文评估用户对该知识点的掌握情况）", example = "BASIC")
    private String understandingLevel;
}