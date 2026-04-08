package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.ProgressAdjustDTO;
import com.example.entity.dto.ProgressTrackDTO;
import com.example.entity.vo.ProgressAdjustVO;
import com.example.entity.vo.ProgressTrackVO;
import com.example.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/progress")
@Tag(name = "2.3 学习进度智能适配模块", description = "记录学习进度并动态调整教学节奏")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @PostMapping("/track")
    @Operation(summary = "2.3.1 学习进度追踪接口", description = "记录学生学习进度、问答交互记录，为适配调整提供数据支撑")
    public RestBean<ProgressTrackVO> trackProgress(@RequestBody ProgressTrackDTO dto) {
        log.info("【学习进度追踪接口】收到请求，userId: {}, lessonId: {}", dto.getUserId(), dto.getLessonId());
        try {
            ProgressTrackVO result = progressService.trackProgress(dto);
            return new RestBean<>(200, "进度追踪成功", result, RestBean.getRequestIdFromMDC());
        } catch (RuntimeException e) {
            // 签名验证失败返回403
            if (e.getMessage() != null && e.getMessage().contains("签名验证")) {
                log.warn("【学习进度追踪接口】签名验证失败: {}", e.getMessage());
                return RestBean.forbidden(e.getMessage());
            }
            // 其他客户端错误返回400
            log.error("【学习进度追踪接口】处理失败: {}", e.getMessage(), e);
            return RestBean.badRequest(e.getMessage());
        } catch (Exception e) {
            // 服务端错误返回500
            log.error("【学习进度追踪接口】系统异常: {}", e.getMessage(), e);
            return RestBean.error("系统异常，请稍后重试");
        }
    }

    @PostMapping("/adjust")
    @Operation(summary = "2.3.2 学习节奏调整接口", description = "基于学生理解程度与学习进度，调整后续讲授节奏")
    public RestBean<ProgressAdjustVO> adjustProgress(@RequestBody ProgressAdjustDTO dto) {
        log.info("【学习节奏调整接口】收到请求，userId: {}, understandingLevel: {}", 
                dto.getUserId(), dto.getUnderstandingLevel());
        try {
            ProgressAdjustVO result = progressService.adjustProgress(dto);
            return new RestBean<>(200, "节奏调整成功", result, RestBean.getRequestIdFromMDC());
        } catch (RuntimeException e) {
            // 签名验证失败返回403
            if (e.getMessage() != null && e.getMessage().contains("签名验证")) {
                log.warn("【学习节奏调整接口】签名验证失败: {}", e.getMessage());
                return RestBean.forbidden(e.getMessage());
            }
            // 其他客户端错误返回400
            log.error("【学习节奏调整接口】处理失败: {}", e.getMessage(), e);
            return RestBean.badRequest(e.getMessage());
        } catch (Exception e) {
            // 服务端错误返回500
            log.error("【学习节奏调整接口】系统异常: {}", e.getMessage(), e);
            return RestBean.error("系统异常，请稍后重试");
        }
    }
}