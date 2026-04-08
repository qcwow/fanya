package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.QaInteractDTO;
import com.example.entity.dto.VoiceToTextDTO;
import com.example.entity.vo.QaVO;
import com.example.entity.vo.VoiceToTextVO;
import com.example.service.QaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/qa")
@Tag(name = "2.2 多模态实时问答模块", description = "处理学生问答交互及语音识别")
public class QaController {

    @Resource
    private QaService qaService;

    @PostMapping("/interact")
    @Operation(summary = "2.2.1 问答交互接口", description = "接收学生提问，返回 AI 生成的解答内容及后续学习建议")
    public RestBean<QaVO> interact(@RequestBody QaInteractDTO dto) {
        try {
            QaVO vo = qaService.interact(dto);
            return RestBean.success(vo);
        } catch (Exception e) {
            log.error("AI 问答交互异常: {}", e.getMessage());
            int code = e.getMessage().contains("签名") ? 403 : 500;
            return RestBean.failure(code, e.getMessage());
        }
    }

    @PostMapping("/voiceToText")
    @Operation(summary = "2.2.2 语音提问识别接口", description = "将语音文件转换为文字，用于后续问答处理")
    public RestBean<VoiceToTextVO> voiceToText(@RequestBody VoiceToTextDTO dto) {
        try {
            VoiceToTextVO vo = qaService.voiceToText(dto);
            return RestBean.success(vo);
        } catch (Exception e) {
            log.error("语音识别失败: {}", e.getMessage());
            int code = e.getMessage().contains("签名") ? 403 : 500;
            return RestBean.failure(code, e.getMessage());
        }
    }
}