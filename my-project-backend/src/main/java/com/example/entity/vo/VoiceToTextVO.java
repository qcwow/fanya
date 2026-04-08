package com.example.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "语音识别响应结果")
public class VoiceToTextVO {
    @Schema(description = "识别后的文字内容", example = "平面假设为什么能简化梁弯曲问题？")
    private String text;

    @Schema(description = "识别置信度(0-1)", example = "0.98")
    private Double confidence;

    @Schema(description = "识别时间戳", example = "2024-05-20 10:05:00")
    private String timestamp;
}
