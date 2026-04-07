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
@Schema(title = "课件解析结果视图对象", description = "返回课件解析后的任务状态、文件元数据及 AI 提取的目录结构")
public class LessonParseVO {

    @Schema(description = "解析任务ID（用于查询生成结果）", example = "parse20240520001")
    private String parseId;

    @Schema(description = "文件元数据详细信息")
    private FileInfo fileInfo;

    @Schema(description = "课件结构预览（AI 解析出的目录/章节结构）")
    private StructurePreview structurePreview;

    @Schema(description = "任务处理状态：processing(处理中), completed(已完成), failed(失败)", example = "processing")
    private String taskStatus;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "文件元数据信息")
    public static class FileInfo {
        @Schema(description = "原始文件名称", example = "材料力学-梁弯曲理论.pptx")
        private String fileName;

        @Schema(description = "文件大小（字节 Byte）", example = "2048000")
        private Long fileSize;

        @Schema(description = "文件总页数", example = "25")
        private Integer pageCount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "课件结构预览")
    public static class StructurePreview {
        @Schema(description = "章节目录列表")
        private List<ChapterInfo> chapters;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "章节信息")
    public static class ChapterInfo {
        @Schema(description = "章节ID", example = "chap001")
        private String chapterId;

        @Schema(description = "章节名称", example = "梁弯曲理论基础")
        private String chapterName;

        @Schema(description = "子章节列表")
        private List<SubChapterInfo> subChapters;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "子章节信息")
    public static class SubChapterInfo {
        @Schema(description = "子章节ID", example = "sub001")
        private String subChapterId;

        @Schema(description = "子章节名称", example = "平面假设的定义")
        private String subChapterName;

        @Schema(description = "是否为重点", example = "true")
        private Boolean isKeyPoint;

        @Schema(description = "对应课件页数范围", example = "3-5")
        private String pageRange;
    }
}