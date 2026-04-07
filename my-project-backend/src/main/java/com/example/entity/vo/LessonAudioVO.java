package com.example.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "语音合成结果视图对象", description = "返回音频文件URL、音频信息及分章节音频信息")
public class LessonAudioVO {

    @Schema(description = "音频ID", example = "audio20240520001")
    private String audioId;

    @Schema(description = "音频文件URL", example = "http://xxx.com/audio/lesson/20240520001.mp3")
    private String audioUrl;

    @Schema(description = "音频详细信息")
    private AudioInfo audioInfo;

    @Schema(description = "分章节音频信息列表")
    private List<SectionAudio> sectionAudios;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "音频详细信息")
    public static class AudioInfo {
        @Schema(description = "总时长（秒）", example = "600")
        private Integer totalDuration;

        @Schema(description = "文件大小（字节）", example = "9600000")
        private Long fileSize;

        @Schema(description = "音频格式", example = "mp3")
        private String format;

        @Schema(description = "比特率", example = "128000")
        private Integer bitRate;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "分章节音频信息")
    public static class SectionAudio {
        @Schema(description = "章节ID", example = "sec001")
        private String sectionId;

        @Schema(description = "章节音频URL", example = "http://xxx.com/audio/section/sec001.mp3")
        private String audioUrl;

        @Schema(description = "章节时长（秒）", example = "15")
        private Integer duration;
    }
}
