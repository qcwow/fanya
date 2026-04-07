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
@Schema(title = "语音合成记录实体", description = "存储脚本转换后的音频文件信息")
@TableName("db_lesson_audio")
public class LessonAudio {

    /**
     * 音频ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "音频ID（自增主键）")
    private Long id;

    /**
     * 关联脚本ID
     */
    @Schema(description = "关联的脚本ID")
    private Long scriptId;

    /**
     * 发音人类型
     */
    @Schema(description = "发音人类型：female_standard、male_professional")
    private String voiceType;

    /**
     * 全本音频URL
     */
    @Schema(description = "全本音频文件访问URL")
    private String audioUrl;

    /**
     * 音频格式
     */
    @Schema(description = "音频格式：mp3、wav")
    private String format;

    /**
     * 比特率
     */
    @Schema(description = "音频比特率")
    private Integer bitRate;

    /**
     * 总时长
     */
    @Schema(description = "音频总时长（秒）")
    private Integer totalDuration;

    /**
     * 文件大小
     */
    @Schema(description = "音频文件大小（字节）")
    private Long fileSize;

    /**
     * 创建时间
     */
    @Schema(description = "记录创建时间")
    private LocalDateTime createTime;
}
