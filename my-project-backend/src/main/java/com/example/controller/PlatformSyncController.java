package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.SyncCourseDTO;
import com.example.entity.dto.SyncUserDTO;
import com.example.entity.vo.SyncCourseVO;
import com.example.entity.vo.SyncUserVO;
import com.example.service.CourseService;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/api/v1/platform")
@Tag(name = "3.0 平台对接预留模块", description = "处理外部教育平台课程与用户信息同步")
public class PlatformSyncController {

    @Resource
    private CourseService courseService;

    @Resource
    private UserService userService;

    @PostMapping("/syncCourse")
    @Operation(summary = "3.1 课程信息同步接口")
    public RestBean<SyncCourseVO> syncCourse(@RequestBody SyncCourseDTO dto) {
        try {
            String internalId = courseService.syncCourse(dto);

            SyncCourseVO vo = SyncCourseVO.builder()
                    .internalCourseId("cou" + internalId) // 模仿文档前缀，也可直接传数字
                    .syncStatus("success")
                    .syncTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .build();

            return RestBean.success(vo);
        } catch (Exception e) {
            log.error("同步课程失败", e);
            return RestBean.failure(403, e.getMessage());
        }
    }
    @PostMapping("/syncUser")
    @Operation(summary = "3.2 用户信息同步接口")
    public RestBean<SyncUserVO> syncUser(@RequestBody @Valid SyncUserDTO dto) {
        try {
            log.info("接收到用户同步请求: 平台ID={}, 用户名={}", dto.getPlatformId(), dto.getUserInfo().getUserName());

            // 调用 Service 处理同步并生成 Token
            SyncUserVO vo = userService.syncUser(dto);

            return RestBean.success(vo);
        } catch (Exception e) {
            log.error("用户信息同步失败: {}", e.getMessage());
            // 同样区分签名错误与其他业务错误
            int code = e.getMessage().contains("签名") ? 403 : 400;
            return RestBean.failure(code, e.getMessage());
        }
    }
}