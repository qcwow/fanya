package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.po.Lesson;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LessonMapper extends BaseMapper<Lesson> {
}
