package com.example.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "问答记录实体", description = "存储用户在学习过程中的问答交互、理解程度及AI建议")
@TableName(value = "db_qa_record", autoResultMap = true)
public class QaRecord {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID（自增）")
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "关联的用户ID")
    private Long userId;

    /**
     * 课程ID
     */
    @Schema(description = "关联的课程ID")
    private Long lessonId;

    /**
     * 会话ID
     */
    @Schema(description = "问答会话标识ID（用于区分不同轮次的对话）")
    private String sessionId;

    /**
     * 问题类型
     */
    @Schema(description = "问题分类/类型")
    private String questionType;

    /**
     * 问题内容
     */
    @Schema(description = "用户提问的具体内容")
    private String questionContent;

    /**
     * 回答内容
     */
    @Schema(description = "系统或AI返回的回答内容")
    private String answerContent;

    /**
     * 当前小节ID
     */
    @Schema(description = "提问时所处的小节ID")
    private Long currentSectionId;

    /**
     * 理解程度
     */
    @Schema(description = "用户对当前知识点的理解程度评价")
    private String understandingLevel;

    /**
     * 关联知识点
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "关联的知识点详情（JSON格式存储）")
    private Map<String, Object> relatedKnowledge;

    /**
     * 学习建议
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "针对该问题的学习建议列表（JSON格式存储）")
    private List<String> suggestions;

    /**
     * 创建时间
     */
    @Schema(description = "记录创建时间")
    private LocalDateTime createTime;
}