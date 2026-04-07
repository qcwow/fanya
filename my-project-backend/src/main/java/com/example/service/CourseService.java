package com.example.service;

import com.example.entity.dto.SyncCourseDTO;

public interface CourseService {
    /**
     * 同步课程并返回内部 ID
     */
    String syncCourse(SyncCourseDTO dto);
}
