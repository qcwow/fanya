package com.example.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "课件解析结果视图对象", description = "返回课件解析后的任务状态、文件元数据及 AI 提取的目录结构")
public class LessonParseVO {

    @Schema(description = "解析记录ID / 任务ID", example = "123456789")
    private String parseId;

    @Schema(description = "文件元数据详细信息")
    private FileInfo fileInfo;

    @Schema(description = "课件结构预览（AI 解析出的目录/章节结构）")
    private StructurePreview structurePreview;

    @Schema(description = "任务处理状态：processing(处理中), completed(已完成), failed(失败)", example = "completed")
    private String taskStatus;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "文件元数据信息")
    public static class FileInfo {
        @Schema(description = "原始文件名称", example = "Java并发编程实战.pdf")
        private String fileName;

        @Schema(description = "文件大小（字节 Byte）", example = "15728640")
        private Long fileSize;

        @Schema(description = "文件总页数", example = "320")
        private Integer pageCount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "课件结构预览")
    public static class StructurePreview {
        @Schema(description = "章节目录列表（每个 Map 包含章节标题、层级、起始页码等信息）")
        private List<Map<String, Object>> chapters;
    }
}