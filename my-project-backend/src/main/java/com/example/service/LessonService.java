package com.example.service;

import com.example.entity.dto.LessonAudioDTO;
import com.example.entity.dto.LessonParseDTO;
import com.example.entity.dto.LessonScriptDTO;
import com.example.entity.vo.LessonAudioVO;
import com.example.entity.vo.LessonParseVO;
import com.example.entity.vo.LessonScriptVO;

public interface LessonService {

    /**
     * 课件上传与解析
     * @param dto 课件解析请求参数
     * @return 课件解析结果
     */
    LessonParseVO parseLesson(LessonParseDTO dto);

    /**
     * 智课脚本生成
     * @param dto 脚本生成请求参数
     * @return 脚本生成结果
     */
    LessonScriptVO generateScript(LessonScriptDTO dto);

    /**
     * 语音合成
     * @param dto 语音合成请求参数
     * @return 语音合成结果
     */
    LessonAudioVO generateAudio(LessonAudioDTO dto);
}
