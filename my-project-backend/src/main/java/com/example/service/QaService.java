package com.example.service;

import com.example.entity.dto.QaInteractDTO;
import com.example.entity.dto.VoiceToTextDTO;
import com.example.entity.vo.QaVO;
import com.example.entity.vo.VoiceToTextVO;

public interface QaService {
    /**
     * AI 问答交互核心方法
     */
    QaVO interact(QaInteractDTO dto);

    VoiceToTextVO voiceToText(VoiceToTextDTO dto);
}