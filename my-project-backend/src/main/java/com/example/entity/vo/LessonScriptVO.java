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
@Schema(title = "课件脚本视图对象", description = "返回 AI 生成的教学脚本、结构化内容以及后续操作的 URL 地址")
public class LessonScriptVO {

    @Schema(description = "脚本记录唯一ID", example = "SCT_9527123")
    private String scriptId;

    @Schema(description = "脚本结构化内容列表")
    private List<ScriptSection> scriptStructure;

    @Schema(description = "在线编辑脚本的页面地址", example = "https://editor.example.com/edit/SCT_9527123")
    private String editUrl;

    @Schema(description = "触发音频生成的接口/页面地址", example = "https://api.example.com/audio/generate?id=SCT_9527123")
    private String audioGenerateUrl;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "脚本小节详情")
    public static class ScriptSection {

        @Schema(description = "小节唯一ID", example = "sec_001")
        private String sectionId;

        @Schema(description = "小节名称/标题", example = "第一章：深度学习基础介绍")
        private String sectionName;

        @Schema(description = "本小节的具体讲解文案/脚本内容", example = "大家好，今天我们来学习深度学习的基本概念...")
        private String content;

        @Schema(description = "预计讲解时长（单位：秒）", example = "120")
        private Integer duration;

        @Schema(description = "关联的课件章节ID（对应 StructurePreview 中的章节）", example = "chap_1")
        private String relatedChapterId;

        @Schema(description = "关联的原始课件页码（如：1-3）", example = "1")
        private String relatedPage;

        @Schema(description = "本小节涵盖的核心知识点列表")
        private List<String> keyPoints;
    }
}