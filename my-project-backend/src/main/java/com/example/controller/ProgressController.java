package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.ProgressAdjustDTO;
import com.example.entity.dto.ProgressTrackDTO;
import com.example.entity.vo.ProgressAdjustVO;
import com.example.entity.vo.ProgressTrackVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/progress")
@Tag(name = "2.3 学习进度智能适配模块", description = "记录学习进度并动态调整教学节奏")
public class ProgressController {

    @PostMapping("/track")
    @Operation(summary = "2.3.1 学习进度追踪接口")
    public RestBean<ProgressTrackVO> trackProgress(@RequestBody ProgressTrackDTO dto) {
        return RestBean.success();
    }

    @PostMapping("/adjust")
    @Operation(summary = "2.3.2 学习节奏调整接口")
    public RestBean<ProgressAdjustVO> adjustProgress(@RequestBody ProgressAdjustDTO dto) {
        return RestBean.success();
    }
}