package com.example.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.dto.ProgressAdjustDTO;
import com.example.entity.dto.ProgressTrackDTO;
import com.example.entity.po.LearningProgress;
import com.example.entity.vo.ProgressAdjustVO;
import com.example.entity.vo.ProgressTrackVO;
import com.example.mapper.LearningProgressMapper;
import com.example.service.ProgressService;
import com.example.utils.SignUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProgressServiceImpl implements ProgressService {

    @Resource
    private LearningProgressMapper learningProgressMapper;

    @Value("${fanya.api.static-key}")
    private String staticKey;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProgressTrackVO trackProgress(ProgressTrackDTO dto) {
        log.info("【学习进度追踪】开始处理，userId: {}, lessonId: {}", dto.getUserId(), dto.getLessonId());

        // 1. 签名验证
        verifySignature(dto);

        // 2. 解析用户ID和智课ID（实际项目中可能需要从字符串转换为Long）
        Long userId = parseUserId(dto.getUserId());
        Long lessonId = parseLessonId(dto.getLessonId());
        Long currentSectionId = parseSectionId(dto.getCurrentSectionId());

        // 3. 查询或创建学习进度记录
        LearningProgress progress = learningProgressMapper.selectOne(
                new LambdaQueryWrapper<LearningProgress>()
                        .eq(LearningProgress::getUserId, userId)
                        .eq(LearningProgress::getLessonId, lessonId)
        );

        if (progress == null) {
            progress = new LearningProgress();
            progress.setUserId(userId);
            progress.setLessonId(lessonId);
            progress.setProgressPercent(0f);
            progress.setTotalProgress(0f);
        }

        // 4. 更新进度信息
        progress.setCurrentSectionId(currentSectionId);
        progress.setProgressPercent(dto.getProgressPercent());
        progress.setLastOperateTime(LocalDateTime.parse(dto.getLastOperateTime(), FORMATTER));

        // 5. 计算总进度（简化逻辑：当前章节进度 * 权重）
        Float totalProgress = calculateTotalProgress(userId, lessonId, dto.getProgressPercent());
        progress.setTotalProgress(totalProgress);

        // 6. 保存或更新
        if (progress.getId() == null) {
            learningProgressMapper.insert(progress);
        } else {
            learningProgressMapper.updateById(progress);
        }

        // 7. 构建响应数据
        ProgressTrackVO vo = new ProgressTrackVO();
        vo.setTrackId("track" + System.currentTimeMillis());
        vo.setTotalProgress(totalProgress);
        vo.setNextSectionSuggest(calculateNextSection(dto.getCurrentSectionId(), dto.getQaRecordId()));

        log.info("【学习进度追踪】处理完成，trackId: {}, totalProgress: {}", vo.getTrackId(), vo.getTotalProgress());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProgressAdjustVO adjustProgress(ProgressAdjustDTO dto) {
        log.info("【学习节奏调整】开始处理，userId: {}, understandingLevel: {}", 
                dto.getUserId(), dto.getUnderstandingLevel());

        // 1. 签名验证
        verifySignature(dto);

        // 2. 根据理解程度确定调整策略
        String adjustType = determineAdjustType(dto.getUnderstandingLevel());

        // 3. 构建调整计划
        ProgressAdjustVO.AdjustPlan adjustPlan = new ProgressAdjustVO.AdjustPlan();
        adjustPlan.setContinueSectionId(dto.getCurrentSectionId());
        adjustPlan.setAdjustType(adjustType);

        // 4. 如果是部分理解，提供补充讲解内容
        if ("partial".equals(dto.getUnderstandingLevel())) {
            ProgressAdjustVO.SupplementContent supplementContent = new ProgressAdjustVO.SupplementContent();
            supplementContent.setContent(generateSupplementContent(dto.getCurrentSectionId()));
            supplementContent.setDuration(30);
            supplementContent.setRelatedExample("工程中常见的简支梁弯曲问题，均基于平面假设推导正应力公式");
            adjustPlan.setSupplementContent(supplementContent);
        }

        // 5. 生成后续章节调整建议
        List<ProgressAdjustVO.NextSection> nextSections = generateNextSections(dto.getCurrentSectionId(), adjustType);
        adjustPlan.setNextSections(nextSections);

        // 6. 构建响应
        ProgressAdjustVO vo = new ProgressAdjustVO();
        vo.setAdjustPlan(adjustPlan);

        log.info("【学习节奏调整】处理完成，adjustType: {}", adjustType);
        return vo;
    }

    /**
     * 签名验证
     */
    private void verifySignature(Object dto) {
        try {
            SignUtils.verifyPlatformSignature(dto, staticKey);
        } catch (Exception e) {
            log.error("【签名验证】失败: {}", e.getMessage());
            throw new RuntimeException("签名验证失败: " + e.getMessage());
        }
    }

    /**
     * 解析用户ID
     */
    private Long parseUserId(String userId) {
        try {
            // 如果userId是纯数字，直接转换；否则需要映射
            return Long.parseLong(userId.replaceAll("\\D+", ""));
        } catch (Exception e) {
            log.warn("【用户ID解析】使用默认值，原始值: {}", userId);
            return 1L;
        }
    }

    /**
     * 解析智课ID
     */
    private Long parseLessonId(String lessonId) {
        try {
            return Long.parseLong(lessonId.replaceAll("\\D+", ""));
        } catch (Exception e) {
            log.warn("【智课ID解析】使用默认值，原始值: {}", lessonId);
            return 1L;
        }
    }

    /**
     * 解析章节ID
     */
    private Long parseSectionId(String sectionId) {
        try {
            return Long.parseLong(sectionId.replaceAll("\\D+", ""));
        } catch (Exception e) {
            log.warn("【章节ID解析】使用默认值，原始值: {}", sectionId);
            return 1L;
        }
    }

    /**
     * 计算总进度
     */
    private Float calculateTotalProgress(Long userId, Long lessonId, Float currentProgress) {
        // 简化逻辑：这里应该查询该课程所有章节的完成情况
        // 暂时返回当前进度的80%作为示例
        return Math.round(currentProgress * 0.8f * 10) / 10.0f;
    }

    /**
     * 计算下一章节建议
     */
    private String calculateNextSection(String currentSectionId, String qaRecordId) {
        // 如果有问答记录且理解程度较低，建议继续当前章节
        if (qaRecordId != null && !qaRecordId.isEmpty()) {
            return currentSectionId;
        }
        // 否则建议下一章节（简化逻辑）
        try {
            int num = Integer.parseInt(currentSectionId.replaceAll("\\D+", ""));
            return "sec" + String.format("%03d", num + 1);
        } catch (Exception e) {
            return "sec003";
        }
    }

    /**
     * 确定调整类型
     */
    private String determineAdjustType(String understandingLevel) {
        if ("none".equals(understandingLevel)) {
            return "supplement"; // 完全不懂，补充讲解
        } else if ("partial".equals(understandingLevel)) {
            return "supplement"; // 部分理解，补充讲解
        } else if ("full".equals(understandingLevel)) {
            return "accelerate"; // 完全理解，加速
        }
        return "normal"; // 默认正常
    }

    /**
     * 生成补充讲解内容
     */
    private String generateSupplementContent(String sectionId) {
        // 实际项目中应该根据章节ID从知识库或AI生成
        return "为了进一步理解平面假设的简化作用，我们以矩形截面梁为例，详细分析其受力特点和变形规律。在工程实践中，这一假设大大简化了复杂结构的应力计算过程。";
    }

    /**
     * 生成后续章节调整建议
     */
    private List<ProgressAdjustVO.NextSection> generateNextSections(String currentSectionId, String adjustType) {
        List<ProgressAdjustVO.NextSection> sections = new ArrayList<>();

        try {
            int currentNum = Integer.parseInt(currentSectionId.replaceAll("\\D+", ""));

            // 当前章节
            ProgressAdjustVO.NextSection currentSection = new ProgressAdjustVO.NextSection();
            currentSection.setSectionId(currentSectionId);
            currentSection.setAdjustedDuration("supplement".equals(adjustType) ? 75 : 45);
            currentSection.setIsKeyPointStrengthen("supplement".equals(adjustType));
            sections.add(currentSection);

            // 下一章节
            ProgressAdjustVO.NextSection nextSection = new ProgressAdjustVO.NextSection();
            nextSection.setSectionId("sec" + String.format("%03d", currentNum + 1));
            nextSection.setAdjustedDuration("accelerate".equals(adjustType) ? 30 : 40);
            nextSection.setIsKeyPointStrengthen(false);
            sections.add(nextSection);

        } catch (Exception e) {
            log.error("【生成章节建议】失败: {}", e.getMessage());
        }

        return sections;
    }
}
