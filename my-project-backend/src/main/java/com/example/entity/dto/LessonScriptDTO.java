package com.example.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "智课脚本生成请求参数", description = "基于课件解析结果生成教学脚本的配置参数")
public class LessonScriptDTO {

    @Schema(description = "课件解析任务ID", example = "parse20240520001", required = true)
    private String parseId;

    @Schema(description = "讲授风格（默认standard）", example = "standard", allowableValues = {"standard", "detailed", "concise"})
    private String teachingStyle = "standard";

    @Schema(description = "语速适配（用于语音合成，默认normal）", example = "normal", allowableValues = {"slow", "normal", "fast"})
    private String speechSpeed = "normal";

    @Schema(description = "自定义开场白", example = "同学们好，今天我们学习梁弯曲理论的核心知识点")
    private String customOpening;

    @Schema(description = "当前时间（格式：yyyy-MM-dd HH:mm:ss）", example = "2024-05-20 10:30:00", required = true)
    private String time;

    @Schema(description = "签名信息（MD5加密）", example = "D7E3F9A2B4C6D8E0F2A3B5C7D9E1F3A5", required = true)
    private String enc;
}
