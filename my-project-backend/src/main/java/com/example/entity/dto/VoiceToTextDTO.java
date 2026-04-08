package com.example.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "语音识别请求参数")
public class VoiceToTextDTO {
    @Schema(description = "语音文件URL", example = "http://xxx.com/voice/123.wav")
    private String voiceUrl;

    @Schema(description = "语音时长（秒）", example = "10")
    private Integer voiceDuration;

    @Schema(description = "语言类型（默认zh-CN）", example = "zh-CN")
    private String language;

    @Schema(description = "签名时间戳", example = "2024-05-2011:00:00")
    private String time;

    @Schema(description = "签名信息")
    private String enc;
}
