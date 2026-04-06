package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.LessonAudioDTO;
import com.example.entity.dto.LessonParseDTO;
import com.example.entity.dto.LessonScriptDTO;
import com.example.entity.vo.LessonAudioVO;
import com.example.entity.vo.LessonParseVO;
import com.example.entity.vo.LessonScriptVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lesson")
@Tag(name = "2.1 智课智能生成模块", description = "处理课件解析、脚本生成及语音合成")
public class LessonController {

    @PostMapping("/parse")
    @Operation(summary = "2.1.1 课件上传与解析接口")
    public RestBean<LessonParseVO> parseLesson(@RequestBody LessonParseDTO dto) {
        // TODO: 校验签名 (dto.getEnc())
        // TODO: 调用 Service 执行解析逻辑
        return RestBean.success();
    }

    @PostMapping("/generateScript")
    @Operation(summary = "2.1.2 智课脚本生成接口")
    public RestBean<LessonScriptVO> generateScript(@RequestBody LessonScriptDTO dto) {
        return RestBean.success();
    }

    @PostMapping("/generateAudio")
    @Operation(summary = "2.1.3 语音合成接口")
    public RestBean<LessonAudioVO> generateAudio(@RequestBody LessonAudioDTO dto) {
        return RestBean.success();
    }
}