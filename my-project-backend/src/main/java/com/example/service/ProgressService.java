package com.example.service;

import com.example.entity.dto.ProgressAdjustDTO;
import com.example.entity.dto.ProgressTrackDTO;
import com.example.entity.vo.ProgressAdjustVO;
import com.example.entity.vo.ProgressTrackVO;

public interface ProgressService {

    /**
     * 学习进度追踪
     * @param dto 进度追踪请求参数
     * @return 进度追踪响应数据
     */
    ProgressTrackVO trackProgress(ProgressTrackDTO dto);

    /**
     * 学习节奏调整
     * @param dto 节奏调整请求参数
     * @return 节奏调整响应数据
     */
    ProgressAdjustVO adjustProgress(ProgressAdjustDTO dto);
}
