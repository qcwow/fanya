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

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "脚本小节内容实体", description = "存储教学脚本的各个小节详细内容")
@TableName(value = "db_lesson_script_section", autoResultMap = true)
public class LessonScriptSection {

    /**
     * 小节ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "小节ID（自增主键）")
    private Long id;

    /**
     * 关联脚本ID
     */
    @Schema(description = "关联的脚本ID")
    private Long scriptId;

    /**
     * 小节名称
     */
    @Schema(description = "小节名称/标题")
    private String sectionName;

    /**
     * 讲授正文内容
     */
    @Schema(description = "本小节的具体讲解文案/脚本内容")
    private String content;

    /**
     * 预计时长(秒)
     */
    @Schema(description = "预计讲解时长（单位：秒）")
    private Integer duration;

    /**
     * 关联课件结构ID
     */
    @Schema(description = "关联的课件章节ID")
    private Long relatedChapterId;

    /**
     * 关联页码
     */
    @Schema(description = "关联的原始课件页码")
    private String relatedPage;

    /**
     * 关键知识点标签(JSON数组)
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "本小节涵盖的核心知识点列表（JSON格式）")
    private List<String> keyPoints;

    /**
     * 排序
     */
    @Schema(description = "排序顺序")
    private Integer sortOrder;
}
