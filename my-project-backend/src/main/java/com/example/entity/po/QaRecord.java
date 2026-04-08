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

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "关联的内部用户ID（数字ID）")
    private Long userId;

    @Schema(description = "所属学校ID")
    private String schoolId; // 新增

    @Schema(description = "关联的课程ID")
    private String courseId; // 新增，且建议改为 String 兼容外部标识

    @Schema(description = "关联的智课/资源ID")
    private String lessonId; // 建议改为 String 兼容外部标识 "lesson2024..."

    @Schema(description = "问答会话标识ID")
    private String sessionId;

    @Schema(description = "问题分类（text或voice）")
    private String questionType;

    @Schema(description = "用户提问的具体内容")
    private String questionContent;

    @Schema(description = "系统或AI返回的回答内容")
    private String answerContent;

    @Schema(description = "回答类型（text:纯文本, mixed:图文）")
    private String answerType; // 新增

    @Schema(description = "提问时所处的小节ID")
    private String currentSectionId; // 建议改为 String 兼容外部标识 "sec002"

    @Schema(description = "用户对当前知识点的理解程度评价")
    private String understandingLevel;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "关联的知识点详情（JSON格式存储）")
    private Map<String, Object> relatedKnowledge;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "针对该问题的学习建议列表（JSON格式存储）")
    private List<String> suggestions;

    @Schema(description = "记录创建时间")
    private LocalDateTime createTime;
}