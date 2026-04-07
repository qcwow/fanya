package com.example.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "问答交互请求参数", description = "封装用户发起AI问答时所需的业务参数、上下文信息及安全校验码")
public class QaInteractDTO {

    @Schema(description = "学校ID", example = "SCH_001")
    private String schoolId;

    @Schema(description = "用户ID", example = "10086")
    private String userId;

    @Schema(description = "课程ID", example = "5001")
    private String courseId;

    @Schema(description = "课件/资源ID", example = "2001")
    private String lessonId;

    @Schema(description = "会话ID（用于维持多轮对话状态，首轮提问可为空）", example = "uuid-xxxx-xxxx")
    private String sessionId;

    @Schema(description = "问题类型（如：KNOWLEDGE_POINT, SUMMARY, PRACTICE等）", example = "KNOWLEDGE_POINT")
    private String questionType;

    @Schema(description = "用户提问的具体文本内容", example = "请解释一下什么是深度学习？")
    private String questionContent;

    @Schema(description = "当前学习的小节或知识切片ID", example = "12")
    private String currentSectionId;

    @Schema(description = "历史问答上下文记录（用于AI理解对话语境）")
    private List<Map<String, String>> historyQa;

    @Schema(description = "加密校验码/签名串（用于接口安全与身份校验）")
    private String enc;
}