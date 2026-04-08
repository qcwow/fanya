package com.example.controller;

import com.example.config.ApiSecurityConfig;
import com.example.entity.RestBean;
import com.example.entity.dto.LessonAudioDTO;
import com.example.entity.dto.LessonParseDTO;
import com.example.entity.dto.LessonScriptDTO;
import com.example.entity.vo.LessonAudioVO;
import com.example.entity.vo.LessonParseVO;
import com.example.entity.vo.LessonScriptVO;
import com.example.service.LessonService;
import com.example.utils.SignUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/lesson")
@Tag(name = "2.1 智课智能生成模块", description = "处理课件解析、脚本生成及语音合成")
public class LessonController {

    @Resource
    private LessonService lessonService;
    
    @Resource
    private ApiSecurityConfig securityConfig;

    @PostMapping("/parse")
    @Operation(summary = "2.1.1 课件上传与解析接口", description = "接收 PPT/PDF 格式课件文件，解析知识点层级、公式图表、重点标注等结构化信息")
    public RestBean<LessonParseVO> parseLesson(@RequestBody LessonParseDTO dto) {
        try {
            // 1. 签名验证（自动校验 enc 和 time）
            SignUtils.verifyPlatformSignature(dto, securityConfig.getStaticKey());
            
            // 2. 参数校验
            if (dto.getSchoolId() == null || dto.getUserId() == null || dto.getCourseId() == null) {
                return RestBean.badRequest("学校ID、用户ID、课程ID不能为空");
            }
            
            // 3. 调用业务逻辑
            LessonParseVO result = lessonService.parseLesson(dto);
            log.info("课件解析成功: parseId={}", result.getParseId());
            return new RestBean<>(200, "课件解析成功", result, RestBean.getRequestIdFromMDC());
            
        } catch (RuntimeException e) {
            // 签名验证失败或其他运行时异常
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("签名验证失败")) {
                log.warn("课件解析接口签名验证失败: schoolId={}, userId={}, 错误: {}", dto.getSchoolId(), dto.getUserId(), errorMsg);
                return RestBean.forbidden(errorMsg);
            }
            log.error("课件解析参数错误: {}", errorMsg, e);
            return RestBean.badRequest(errorMsg);
        } catch (Exception e) {
            log.error("课件解析失败", e);
            return RestBean.error("课件解析失败: " + e.getMessage());
        }
    }

    @PostMapping("/generateScript")
    @Operation(summary = "2.1.2 智课脚本生成接口", description = "基于课件解析结果，生成符合教学逻辑的结构化讲授脚本")
    public RestBean<LessonScriptVO> generateScript(@RequestBody LessonScriptDTO dto) {
        try {
            // 1. 签名验证（自动校验 enc 和 time）
            SignUtils.verifyPlatformSignature(dto, securityConfig.getStaticKey());
            
            // 2. 参数校验
            if (dto.getParseId() == null || dto.getParseId().isEmpty()) {
                return RestBean.badRequest("解析任务ID不能为空");
            }
            
            // 3. 调用业务逻辑
            LessonScriptVO result = lessonService.generateScript(dto);
            log.info("脚本生成成功: scriptId={}", result.getScriptId());
            return new RestBean<>(200, "脚本生成成功", result, RestBean.getRequestIdFromMDC());
            
        } catch (RuntimeException e) {
            // 签名验证失败或其他运行时异常
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("签名验证失败")) {
                log.warn("脚本生成接口签名验证失败: parseId={}, 错误: {}", dto.getParseId(), errorMsg);
                return RestBean.forbidden(errorMsg);
            }
            log.error("脚本生成参数错误: {}", errorMsg, e);
            return RestBean.badRequest(errorMsg);
        } catch (Exception e) {
            log.error("脚本生成失败", e);
            return RestBean.error("脚本生成失败: " + e.getMessage());
        }
    }

    @PostMapping("/generateAudio")
    @Operation(summary = "2.1.3 语音合成接口", description = "将结构化脚本转换为语音音频，支持对接通用语音合成工具")
    public RestBean<LessonAudioVO> generateAudio(@RequestBody LessonAudioDTO dto) {
        try {
            // 1. 签名验证（自动校验 enc 和 time）
            SignUtils.verifyPlatformSignature(dto, securityConfig.getStaticKey());
            
            // 2. 参数校验
            if (dto.getScriptId() == null || dto.getScriptId().isEmpty()) {
                return RestBean.badRequest("脚本ID不能为空");
            }
            
            // 3. 调用业务逻辑
            LessonAudioVO result = lessonService.generateAudio(dto);
            log.info("语音合成成功: audioId={}", result.getAudioId());
            return new RestBean<>(200, "语音合成成功", result, RestBean.getRequestIdFromMDC());
            
        } catch (RuntimeException e) {
            // 签名验证失败或其他运行时异常
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("签名验证失败")) {
                log.warn("语音合成接口签名验证失败: scriptId={}, 错误: {}", dto.getScriptId(), errorMsg);
                return RestBean.forbidden(errorMsg);
            }
            log.error("语音合成参数错误: {}", errorMsg, e);
            return RestBean.badRequest(errorMsg);
        } catch (Exception e) {
            log.error("语音合成失败", e);
            return RestBean.error("语音合成失败: " + e.getMessage());
        }
    }
}
