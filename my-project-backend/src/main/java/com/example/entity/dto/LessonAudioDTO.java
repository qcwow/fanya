package com.example.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "语音合成请求参数", description = "将结构化脚本转换为语音音频的配置参数")
public class LessonAudioDTO {

    @Schema(description = "脚本ID", example = "script20240520001", required = true)
    private String scriptId;

    @Schema(description = "语音类型（默认female_standard）", example = "female_standard", allowableValues = {"female_standard", "male_professional"})
    private String voiceType = "female_standard";

    @Schema(description = "音频格式（默认mp3）", example = "mp3", allowableValues = {"mp3", "wav"})
    private String audioFormat = "mp3";

    @Schema(description = "指定合成的章节ID（默认全部）", example = "[\"sec001\", \"sec002\"]")
    private List<String> sectionIds;

    @Schema(description = "当前时间（格式：yyyy-MM-dd HH:mm:ss）", example = "2024-05-20 10:30:00", required = true)
    private String time;

    @Schema(description = "签名信息（MD5加密）", example = "E8F9A3B5C7D9E1F3A5B7C9D1E3F5A7B9", required = true)
    private String enc;
}
