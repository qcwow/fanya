package com.example.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("db_learning_progress")
@Schema(title = "学习进度追踪实体", description = "记录学生学习进度、问答交互记录，为适配调整提供数据支撑")
public class LearningProgress {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "学生ID")
    private Long userId;

    @Schema(description = "智课ID")
    private Long lessonId;

    @Schema(description = "当前学习的小节ID")
    private Long currentSectionId;

    @Schema(description = "章节学习进度(0-100)")
    private Float progressPercent;

    @Schema(description = "整门课总进度")
    private Float totalProgress;

    @Schema(description = "最后操作时间")
    private LocalDateTime lastOperateTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
