package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.dto.SyncCourseDTO;
import com.example.entity.po.Course;
import com.example.mapper.CourseMapper;
import com.example.service.CourseService;
import com.example.utils.SignUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;


@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    @Resource
    private CourseMapper courseMapper;

    // 修复点 1：确保使用的是 org.springframework.beans.factory.annotation.Value
    @Value("${fanya.api.static-key}")
    private String staticKey;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String syncCourse(SyncCourseDTO dto) {
        // 1. 签名校验
        SignUtils.verifyPlatformSignature(dto, staticKey);

        SyncCourseDTO.CourseInfoDTO info = dto.getCourseInfo();

        // 3. 检查是否存在 (根据 platformId + platformCourseId 唯一确定)
        Course existing = courseMapper.selectOne(new LambdaQueryWrapper<Course>()
                .eq(Course::getPlatformId, dto.getPlatformId())
                .eq(Course::getPlatformCourseId, info.getCourseId()));

        if (existing != null) {
            copyProperties(info, existing);
            courseMapper.updateById(existing);
            return String.valueOf(existing.getId());
        } else {
            Course newCourse = new Course();
            newCourse.setPlatformId(dto.getPlatformId());
            newCourse.setPlatformCourseId(info.getCourseId());
            newCourse.setCreateTime(LocalDateTime.now());
            copyProperties(info, newCourse);
            courseMapper.insert(newCourse);
            return String.valueOf(newCourse.getId());
        }
    }

    private void copyProperties(SyncCourseDTO.CourseInfoDTO source, Course target) {
        target.setCourseName(source.getCourseName());
        target.setSchoolId(source.getSchoolId());
        target.setSchoolName(source.getSchoolName());
        target.setTerm(source.getTerm());
        target.setCredit(source.getCredit());
        target.setPeriod(source.getPeriod());
        target.setCourseCover(source.getCourseCover());
        target.setTeacherInfo(source.getTeacherInfo());
    }
}