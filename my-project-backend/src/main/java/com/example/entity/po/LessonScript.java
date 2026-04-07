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
@Schema(title = "智课脚本实体", description = "存储 AI 生成的教学脚本主表信息")
@TableName("db_lesson_script")
public class LessonScript {

    /**
     * 脚本ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "脚本ID（自增主键）")
    private Long id;

    /**
     * 关联智课ID
     */
    @Schema(description = "关联的智课ID")
    private Long lessonId;

    /**
     * 讲授风格
     */
    @Schema(description = "讲授风格：standard(标准)、detailed(详细)、concise(简洁)")
    private String teachingStyle;

    /**
     * 语速
     */
    @Schema(description = "语速：slow、normal、fast")
    private String speechSpeed;

    /**
     * 自定义开场白
     */
    @Schema(description = "自定义开场白内容")
    private String customOpening;

    /**
     * 创建时间
     */
    @Schema(description = "记录创建时间")
    private LocalDateTime createTime;
}
