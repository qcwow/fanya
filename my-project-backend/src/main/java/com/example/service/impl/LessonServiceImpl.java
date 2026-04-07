package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.dto.LessonAudioDTO;
import com.example.entity.dto.LessonParseDTO;
import com.example.entity.dto.LessonScriptDTO;
import com.example.entity.po.*;
import com.example.entity.vo.LessonAudioVO;
import com.example.entity.vo.LessonParseVO;
import com.example.entity.vo.LessonScriptVO;
import com.example.mapper.*;
import com.example.service.LessonService;
import com.example.utils.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonMapper lessonMapper;

    @Autowired
    private LessonStructureMapper lessonStructureMapper;

    @Autowired
    private LessonScriptMapper lessonScriptMapper;

    @Autowired
    private LessonScriptSectionMapper lessonScriptSectionMapper;

    @Autowired
    private LessonAudioMapper lessonAudioMapper;

    @Autowired
    private SnowflakeIdGenerator idGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LessonParseVO parseLesson(LessonParseDTO dto) {
        log.info("开始解析课件: schoolId={}, userId={}, courseId={}, fileType={}",
                dto.getSchoolId(), dto.getUserId(), dto.getCourseId(), dto.getFileType());

        // TODO: 校验签名 (dto.getEnc())
        // validateSignature(dto);

        // 1. 创建智课记录
        Lesson lesson = new Lesson();
        lesson.setCourseId(dto.getCourseId());
        lesson.setUserId(dto.getUserId());
        lesson.setFileType(dto.getFileType());
        lesson.setFileUrl(dto.getFileUrl());
        lesson.setTaskStatus("processing");
        lesson.setCreateTime(LocalDateTime.now());

        // 从 URL 中提取文件名（简化处理）
        String fileName = extractFileName(dto.getFileUrl());
        lesson.setFileName(fileName);

        lessonMapper.insert(lesson);
        // 使用数据库自增ID作为内部标识，返回时使用字符串格式
        Long lessonId = lesson.getId();
        String parseId = "parse" + lessonId;

        log.info("智课记录创建成功: lessonId={}", lessonId);

        // 2. 模拟 AI 解析结果（实际应调用 AI 服务）
        // 这里生成模拟的章节结构数据
        List<LessonStructure> structures = generateMockStructures(lessonId);
        for (LessonStructure structure : structures) {
            lessonStructureMapper.insert(structure);
        }

        log.info("课件结构解析完成: 共{}个节点", structures.size());

        // 3. 构建返回结果
        LessonParseVO parseVO = buildParseVO(lesson, structures, parseId);

        // 注意：实际场景中可能是异步任务，这里为了演示直接返回 completed 状态
        parseVO.setTaskStatus("completed");

        return parseVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LessonScriptVO generateScript(LessonScriptDTO dto) {
        log.info("开始生成脚本: parseId={}, teachingStyle={}", dto.getParseId(), dto.getTeachingStyle());

        // TODO: 校验签名
        // validateSignature(dto);

        // 1. 根据 parseId 查找对应的 lesson（parseId 格式为 "parse" + lessonId）
        String parseId = dto.getParseId();
        Long lessonId;
        try {
            // 移除 "parse" 前缀，提取数字部分
            lessonId = Long.parseLong(parseId.replace("parse", ""));
        } catch (NumberFormatException e) {
            throw new RuntimeException("无效的解析任务ID: " + parseId);
        }
        
        Lesson lesson = lessonMapper.selectById(lessonId);
        if (lesson == null) {
            throw new RuntimeException("未找到对应的课件记录: " + dto.getParseId());
        }

        // 2. 创建脚本记录
        LessonScript script = new LessonScript();
        script.setLessonId(lessonId);
        script.setTeachingStyle(dto.getTeachingStyle());
        script.setSpeechSpeed(dto.getSpeechSpeed());
        script.setCustomOpening(dto.getCustomOpening());
        script.setCreateTime(LocalDateTime.now());

        lessonScriptMapper.insert(script);
        Long scriptId = script.getId();

        log.info("脚本记录创建成功: scriptId={}", scriptId);

        // 3. 查询课件结构
        LambdaQueryWrapper<LessonStructure> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LessonStructure::getLessonId, lessonId);
        wrapper.orderByAsc(LessonStructure::getSortOrder);
        List<LessonStructure> structures = lessonStructureMapper.selectList(wrapper);

        // 4. 基于课件结构生成脚本小节（模拟 AI 生成）
        List<LessonScriptSection> sections = generateMockScriptSections(scriptId, structures, dto);
        for (LessonScriptSection section : sections) {
            lessonScriptSectionMapper.insert(section);
        }

        log.info("脚本小节生成完成: 共{}个小节", sections.size());

        // 5. 构建返回结果
        return buildScriptVO(script, sections);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LessonAudioVO generateAudio(LessonAudioDTO dto) {
        log.info("开始语音合成: scriptId={}, voiceType={}", dto.getScriptId(), dto.getVoiceType());

        // TODO: 校验签名
        // validateSignature(dto);

        // 1. 查找脚本（scriptId 格式为 "script" + scriptId）
        String scriptIdStr = dto.getScriptId();
        Long scriptId;
        try {
            // 移除 "script" 前缀，提取数字部分
            scriptId = Long.parseLong(scriptIdStr.replace("script", ""));
        } catch (NumberFormatException e) {
            throw new RuntimeException("无效的脚本ID: " + scriptIdStr);
        }
        
        LessonScript script = lessonScriptMapper.selectById(scriptId);
        if (script == null) {
            throw new RuntimeException("未找到对应的脚本记录: " + dto.getScriptId());
        }

        // 2. 查询脚本小节
        LambdaQueryWrapper<LessonScriptSection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LessonScriptSection::getScriptId, scriptId);
        wrapper.orderByAsc(LessonScriptSection::getSortOrder);

        // 如果指定了 sectionIds，则只合成指定的小节
        if (dto.getSectionIds() != null && !dto.getSectionIds().isEmpty()) {
            List<Long> sectionIds = dto.getSectionIds().stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            wrapper.in(LessonScriptSection::getId, sectionIds);
        }

        List<LessonScriptSection> sections = lessonScriptSectionMapper.selectList(wrapper);

        // 3. 模拟语音合成（实际应调用 TTS 服务）
        String audioUrl = generateMockAudioUrl(scriptId, dto.getAudioFormat());
        Integer totalDuration = sections.stream()
                .mapToInt(s -> s.getDuration() != null ? s.getDuration() : 0)
                .sum();

        // 4. 创建音频记录
        LessonAudio audio = new LessonAudio();
        audio.setScriptId(scriptId);
        audio.setVoiceType(dto.getVoiceType());
        audio.setAudioUrl(audioUrl);
        audio.setFormat(dto.getAudioFormat());
        audio.setBitRate(128000);
        audio.setTotalDuration(totalDuration);
        audio.setFileSize((long) totalDuration * 16000); // 估算文件大小
        audio.setCreateTime(LocalDateTime.now());

        lessonAudioMapper.insert(audio);
        Long audioId = audio.getId();

        log.info("语音合成完成: audioId={}, totalDuration={}s", audioId, totalDuration);

        // 5. 构建返回结果
        return buildAudioVO(audio, sections);
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 从 URL 中提取文件名
     */
    private String extractFileName(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return "unknown.pptx";
        }
        int lastSlash = fileUrl.lastIndexOf('/');
        if (lastSlash >= 0 && lastSlash < fileUrl.length() - 1) {
            return fileUrl.substring(lastSlash + 1);
        }
        return "unknown.pptx";
    }

    /**
     * 生成模拟的课件结构数据
     */
    private List<LessonStructure> generateMockStructures(Long lessonId) {
        List<LessonStructure> structures = new ArrayList<>();

        // 第一章
        LessonStructure chapter1 = new LessonStructure();
        chapter1.setLessonId(lessonId);
        chapter1.setParentId(0L);
        chapter1.setTitle("梁弯曲理论基础");
        chapter1.setIsKeyPoint(false);
        chapter1.setPageRange("1-10");
        chapter1.setSortOrder(1);
        structures.add(chapter1);

        // 1.1 小节
        LessonStructure sub1_1 = new LessonStructure();
        sub1_1.setLessonId(lessonId);
        sub1_1.setParentId(1L); // 假设 chapter1 的 ID 为 1
        sub1_1.setTitle("平面假设的定义");
        sub1_1.setIsKeyPoint(true);
        sub1_1.setPageRange("3-5");
        sub1_1.setSortOrder(1);
        structures.add(sub1_1);

        // 1.2 小节
        LessonStructure sub1_2 = new LessonStructure();
        sub1_2.setLessonId(lessonId);
        sub1_2.setParentId(1L);
        sub1_2.setTitle("弯曲应力计算");
        sub1_2.setIsKeyPoint(true);
        sub1_2.setPageRange("6-10");
        sub1_2.setSortOrder(2);
        structures.add(sub1_2);

        return structures;
    }

    /**
     * 构建课件解析返回对象
     */
    private LessonParseVO buildParseVO(Lesson lesson, List<LessonStructure> structures, String parseId) {
        // 构建章节树
        List<LessonParseVO.ChapterInfo> chapters = new ArrayList<>();

        // 找出所有根节点
        List<LessonStructure> rootNodes = structures.stream()
                .filter(s -> s.getParentId() == 0)
                .collect(Collectors.toList());

        for (LessonStructure root : rootNodes) {
            LessonParseVO.ChapterInfo chapter = new LessonParseVO.ChapterInfo();
            chapter.setChapterId("chap" + root.getId());
            chapter.setChapterName(root.getTitle());

            // 找出子节点
            List<LessonParseVO.SubChapterInfo> subChapters = structures.stream()
                    .filter(s -> s.getParentId().equals(root.getId()))
                    .map(s -> {
                        LessonParseVO.SubChapterInfo sub = new LessonParseVO.SubChapterInfo();
                        sub.setSubChapterId("sub" + s.getId());
                        sub.setSubChapterName(s.getTitle());
                        sub.setIsKeyPoint(s.getIsKeyPoint());
                        sub.setPageRange(s.getPageRange());
                        return sub;
                    })
                    .collect(Collectors.toList());

            chapter.setSubChapters(subChapters);
            chapters.add(chapter);
        }

        LessonParseVO.StructurePreview preview = new LessonParseVO.StructurePreview();
        preview.setChapters(chapters);

        return LessonParseVO.builder()
                .parseId(parseId)
                .fileInfo(new LessonParseVO.FileInfo(
                        lesson.getFileName(),
                        lesson.getFileSize(),
                        lesson.getPageCount()
                ))
                .structurePreview(preview)
                .taskStatus(lesson.getTaskStatus())
                .build();
    }

    /**
     * 生成模拟的脚本小节
     */
    private List<LessonScriptSection> generateMockScriptSections(Long scriptId,
                                                                   List<LessonStructure> structures,
                                                                   LessonScriptDTO dto) {
        List<LessonScriptSection> sections = new ArrayList<>();
        int sortOrder = 0;

        // 如果有自定义开场白，添加开场白小节
        if (dto.getCustomOpening() != null && !dto.getCustomOpening().isEmpty()) {
            LessonScriptSection opening = new LessonScriptSection();
            opening.setScriptId(scriptId);
            opening.setSectionName("开场白");
            opening.setContent(dto.getCustomOpening());
            opening.setDuration(15);
            opening.setSortOrder(sortOrder++);
            sections.add(opening);
        }

        // 为每个知识点生成脚本内容
        for (LessonStructure structure : structures) {
            if (structure.getParentId() != 0) { // 只处理子章节
                LessonScriptSection section = new LessonScriptSection();
                section.setScriptId(scriptId);
                section.setSectionName(structure.getTitle());
                section.setContent(generateMockContent(structure.getTitle(), dto.getTeachingStyle()));
                section.setDuration(45);
                section.setRelatedChapterId(structure.getId());
                section.setRelatedPage(structure.getPageRange());
                section.setKeyPoints(Arrays.asList(
                        structure.getTitle() + "的核心内涵",
                        structure.getTitle() + "的工程意义"
                ));
                section.setSortOrder(sortOrder++);
                sections.add(section);
            }
        }

        return sections;
    }

    /**
     * 生成模拟的脚本内容
     */
    private String generateMockContent(String topic, String style) {
        switch (style) {
            case "detailed":
                return topic + "是本课程的重点内容。首先我们来详细讲解其定义和原理..." ;
            case "concise":
                return topic + "的要点是...";
            default:
                return topic + "是本课程的重要内容，我们需要掌握其基本概念和应用方法。";
        }
    }

    /**
     * 构建脚本返回对象
     */
    private LessonScriptVO buildScriptVO(LessonScript script, List<LessonScriptSection> sections) {
        List<LessonScriptVO.ScriptSection> scriptSections = sections.stream()
                .map(s -> {
                    LessonScriptVO.ScriptSection vo = new LessonScriptVO.ScriptSection();
                    vo.setSectionId("sec" + s.getId());
                    vo.setSectionName(s.getSectionName());
                    vo.setContent(s.getContent());
                    vo.setDuration(s.getDuration());
                    vo.setRelatedChapterId(s.getRelatedChapterId() != null ? "sub" + s.getRelatedChapterId() : "");
                    vo.setRelatedPage(s.getRelatedPage());
                    vo.setKeyPoints(s.getKeyPoints());
                    return vo;
                })
                .collect(Collectors.toList());

        return LessonScriptVO.builder()
                .scriptId("script" + script.getId())
                .scriptStructure(scriptSections)
                .editUrl("http://xxx.com/script/edit?scriptId=script" + script.getId())
                .audioGenerateUrl("http://xxx.com/api/v1/lesson/generateAudio")
                .build();
    }

    /**
     * 生成模拟的音频 URL
     */
    private String generateMockAudioUrl(Long scriptId, String format) {
        return "http://xxx.com/audio/lesson/" + scriptId + "." + format;
    }

    /**
     * 构建音频返回对象
     */
    private LessonAudioVO buildAudioVO(LessonAudio audio, List<LessonScriptSection> sections) {
        List<LessonAudioVO.SectionAudio> sectionAudios = sections.stream()
                .map(s -> {
                    LessonAudioVO.SectionAudio sa = new LessonAudioVO.SectionAudio();
                    sa.setSectionId("sec" + s.getId());
                    sa.setAudioUrl("http://xxx.com/audio/section/sec" + s.getId() + "." + audio.getFormat());
                    sa.setDuration(s.getDuration());
                    return sa;
                })
                .collect(Collectors.toList());

        LessonAudioVO.AudioInfo audioInfo = new LessonAudioVO.AudioInfo();
        audioInfo.setTotalDuration(audio.getTotalDuration());
        audioInfo.setFileSize(audio.getFileSize());
        audioInfo.setFormat(audio.getFormat());
        audioInfo.setBitRate(audio.getBitRate());

        return LessonAudioVO.builder()
                .audioId("audio" + audio.getId())
                .audioUrl(audio.getAudioUrl())
                .audioInfo(audioInfo)
                .sectionAudios(sectionAudios)
                .build();
    }
}
