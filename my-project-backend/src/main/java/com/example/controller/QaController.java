package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.QaInteractDTO;
import com.example.entity.dto.VoiceToTextDTO;
import com.example.entity.vo.QaVO;
import com.example.entity.vo.VoiceToTextVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/qa")
@Tag(name = "2.2 多模态实时问答模块", description = "处理学生问答交互及语音识别")
public class QaController {

    @PostMapping("/interact")
    @Operation(summary = "2.2.1 问答交互接口")
    public RestBean<QaVO> interact(@RequestBody QaInteractDTO dto) {
        // TODO: 签名校验及逻辑实现
        return RestBean.success();
    }

    @PostMapping("/voiceToText")
    @Operation(summary = "2.2.2 语音提问识别接口")
    public RestBean<VoiceToTextVO> voiceToText(@RequestBody VoiceToTextDTO dto) {
        return RestBean.success();
    }
}