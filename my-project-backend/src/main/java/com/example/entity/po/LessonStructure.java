package com.example.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "课件章节结构实体", description = "存储课件解析后的章节层级结构")
@TableName("db_lesson_structure")
public class LessonStructure {

    /**
     * 结构ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "结构ID（自增主键）")
    private Long id;

    /**
     * 关联智课ID
     */
    @Schema(description = "关联的智课ID")
    private Long lessonId;

    /**
     * 父节点ID
     */
    @Schema(description = "父节点ID（0表示根节点）")
    private Long parentId;

    /**
     * 章节/知识点名称
     */
    @Schema(description = "章节或知识点名称")
    private String title;

    /**
     * 是否为重点
     */
    @Schema(description = "是否为重点知识点")
    private Boolean isKeyPoint;

    /**
     * 对应课件页码范围
     */
    @Schema(description = "对应课件页码范围（如：3-5）")
    private String pageRange;

    /**
     * 排序
     */
    @Schema(description = "排序顺序")
    private Integer sortOrder;
}
