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
@Schema(title = "课程信息实体", description = "存储课程的基础信息、所属学校及授课教师数据")
@TableName(value = "db_course", autoResultMap = true)
public class Course {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID（自增）")
    private Long id;

    /**
     * 外部平台课程ID
     */
    @Schema(description = "外部平台关联的课程ID")
    private String platformCourseId;

    /**
     * 外部平台ID
     */
    @Schema(description = "外部平台标识")
    private String platformId;

    /**
     * 课程名称
     */
    @Schema(description = "课程名称")
    private String courseName;

    /**
     * 学校ID
     */
    @Schema(description = "所属学校ID")
    private String schoolId;

    /**
     * 学校名称
     */
    @Schema(description = "所属学校名称")
    private String schoolName;

    /**
     * 学期
     */
    @Schema(description = "开课学期（如：2023-2024第1学期）")
    private String term;

    /**
     * 学分
     */
    @Schema(description = "课程学分")
    private Float credit;

    /**
     * 学时
     */
    @Schema(description = "课程总学时")
    private Integer period;

    /**
     * 课程封面
     */
    @Schema(description = "课程封面图片访问地址")
    private String courseCover;

    /**
     * 教师信息
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "授课教师列表（JSON格式，存储教师姓名、工号等信息）")
    private List<Map<String, String>> teacherInfo;

    /**
     * 创建时间
     */
    @Schema(description = "记录创建时间")
    private LocalDateTime createTime;
}