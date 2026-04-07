package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.SyncCourseDTO;
import com.example.entity.dto.SyncUserDTO;
import com.example.entity.vo.SyncCourseVO;
import com.example.entity.vo.SyncUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/platform")
@Tag(name = "3.0 平台对接预留模块", description = "处理外部教育平台课程与用户信息同步")
public class PlatformSyncController {

    @PostMapping("/syncCourse")
    @Operation(summary = "3.1 课程信息同步接口")
    public RestBean<SyncCourseVO> syncCourse(@RequestBody SyncCourseDTO dto) {
        return RestBean.success();
    }

    @PostMapping("/syncUser")
    @Operation(summary = "3.2 用户信息同步接口")
    public RestBean<SyncUserVO> syncUser(@RequestBody SyncUserDTO dto) {
        return RestBean.success();
    }
}