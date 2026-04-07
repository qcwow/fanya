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
@Schema(title = "课件资源实体", description = "存储用户上传的课程文件、解析状态及相关元数据")
@TableName("db_lesson")
public class Lesson {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID（自增）")
    private Long id;

    /**
     * 课程ID
     */
    @Schema(description = "关联的课程ID")
    private String courseId;

    /**
     * 用户ID
     */
    @Schema(description = "上传者用户ID")
    private String userId;

    /**
     * 文件名称
     */
    @Schema(description = "原始文件名称")
    private String fileName;

    /**
     * 文件大小
     */
    @Schema(description = "文件大小（字节 Byte）")
    private Long fileSize;

    /**
     * 文件类型
     */
    @Schema(description = "文件扩展名或MIME类型")
    private String fileType;

    /**
     * 文件地址
     */
    @Schema(description = "文件在对象存储中的访问URL")
    private String fileUrl;

    /**
     * 页数
     */
    @Schema(description = "课件总页数（如PDF页数）")
    private Integer pageCount;

    /**
     * 任务状态
     */
    @Schema(description = "处理状态：processing(处理中), completed(已完成), failed(失败)")
    private String taskStatus;

    /**
     * 创建时间
     */
    @Schema(description = "记录创建时间")
    private LocalDateTime createTime;
}