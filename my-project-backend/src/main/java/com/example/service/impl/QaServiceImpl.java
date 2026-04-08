package com.example.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.dto.QaInteractDTO;
import com.example.entity.dto.VoiceToTextDTO;
import com.example.entity.po.QaRecord;
import com.example.entity.po.User;
import com.example.entity.vo.QaVO;
import com.example.entity.vo.VoiceToTextVO;
import com.example.mapper.QaRecordMapper;
import com.example.mapper.UserMapper;
import com.example.service.QaService;
import com.example.utils.SignUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class QaServiceImpl implements QaService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private QaRecordMapper qaRecordMapper;

    @Value("${fanya.api.static-key}")
    private String staticKey;
    // --- DeepSeek 配置 ---
    @Value("${fanya.ai.deepseek.api-key}")
    private String apiKey;

    @Value("${fanya.ai.deepseek.url}")
    private String apiUrl;

    @Value("${fanya.ai.deepseek.model}")
    private String modelName;
    // --- SiliconFlow 语音配置 ---
    @Value("${fanya.ai.silicon.api-key}")
    private String siliconApiKey;

    @Value("${fanya.ai.silicon.url}")
    private String siliconApiUrl;

    @Value("${fanya.ai.silicon.model}")
    private String siliconModel;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QaVO interact(QaInteractDTO dto) {
        SignUtils.verifyPlatformSignature(dto, staticKey);

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPlatformUserId, dto.getUserId()));

        if (user == null) {
            throw new RuntimeException("该用户尚未同步到本系统，无法提问");
        }

        String aiResult = this.callAiService(dto.getQuestionContent(), dto.getHistoryQa());

        QaRecord record = new QaRecord();
        record.setUserId(user.getId());
        record.setSchoolId(dto.getSchoolId());
        record.setCourseId(dto.getCourseId());
        record.setLessonId(dto.getLessonId());
        record.setSessionId(dto.getSessionId());
        record.setQuestionType(dto.getQuestionType());
        record.setQuestionContent(dto.getQuestionContent());
        record.setAnswerContent(aiResult);
        record.setAnswerType("text");
        record.setCurrentSectionId(dto.getCurrentSectionId());
        record.setUnderstandingLevel("partial");
        record.setCreateTime(LocalDateTime.now());

        record.setRelatedKnowledge(Map.of(
                "knowledgeId", "know001",
                "knowledgeName", "平面假设的工程简化意义",
                "relatedSectionId", dto.getCurrentSectionId()
        ));
        record.setSuggestions(List.of("想了解更多细节吗？", "需要相关例题吗？"));

        qaRecordMapper.insert(record);

        // 5. 封装 VO 返回
        return QaVO.builder()
                .answerId("ans_" + record.getId())
                .answerContent(aiResult)
                .answerType("text")
                .understandingLevel(record.getUnderstandingLevel())
                .suggestions(record.getSuggestions())
                .relatedKnowledge(QaVO.RelatedKnowledge.builder()
                        .knowledgeId("know001")
                        .knowledgeName("平面假设的工程简化意义")
                        .relatedSectionId(dto.getCurrentSectionId())
                        .build())
                .build();
    }

    /**
     * 对接 DeepSeek AI
     */
    private String callAiService(String question, List<Map<String, String>> history) {
        try {
            JSONArray messages = new JSONArray();

            messages.add(new JSONObject()
                    .set("role", "system")
                    .set("content", "你是一位专业的在线教育助教，来自泛雅互动智课系统。请结合课程背景，用专业、耐心、简洁的语言回答学生的问题。"));

            if (history != null && !history.isEmpty()) {
                for (Map<String, String> qa : history) {
                    messages.add(new JSONObject().set("role", "user").set("content", qa.get("question")));
                    messages.add(new JSONObject().set("role", "assistant").set("content", qa.get("answer")));
                }
            }

            messages.add(new JSONObject().set("role", "user").set("content", question));
            JSONObject body = new JSONObject();
            body.set("model", modelName);
            body.set("messages", messages);
            body.set("stream", false);

            String result = HttpRequest.post(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(JSONUtil.toJsonStr(body))
                    .execute()
                    .body();

            JSONObject responseJson = JSONUtil.parseObj(result);

            if(responseJson.containsKey("error")) {
                log.error("AI 接口调用报错: {}", responseJson.getStr("message"));
                return "抱歉，助教开小差了，请稍后再试。";
            }

            return responseJson.getByPath("choices[0].message.content", String.class);

        } catch (Exception e) {
            log.error("DeepSeek 调用异常", e);
            return "AI 服务连接失败，请检查网络或配置。";
        }
    }

    @Override
    public VoiceToTextVO voiceToText(VoiceToTextDTO dto) {
        SignUtils.verifyPlatformSignature(dto, staticKey);

        log.info("接收到语音识别请求，文件地址: {}", dto.getVoiceUrl());

        String suffix = FileUtil.getSuffix(dto.getVoiceUrl());
        if (suffix == null || suffix.isEmpty()) suffix = "wav";

        String tempPath = System.getProperty("java.io.tmpdir") + File.separator + "fanya_" + System.currentTimeMillis() + "." + suffix;
        File voiceFile = null;

        try {
            log.info("正在下载语音文件到临时路径: {}", tempPath);

            long bytes = HttpRequest.get(dto.getVoiceUrl())
                    .setFollowRedirects(true)
                    .timeout(30000)
                    .execute()
                    .writeBody(FileUtil.file(tempPath));

            if (bytes <= 0) {
                throw new RuntimeException("语音文件下载失败，获取到的内容为空");
            }

            voiceFile = new File(tempPath);
            log.info("文件下载成功，大小: {} 字节", voiceFile.length());
            String result = HttpRequest.post(siliconApiUrl)
                    .header("Authorization", "Bearer " + siliconApiKey)
                    .form("file", voiceFile)
                    .form("model", siliconModel)
                    .timeout(60000)
                    .execute()
                    .body();

            log.info("SiliconFlow 语音识别响应: {}", result);
            JSONObject responseJson = JSONUtil.parseObj(result);

            if (responseJson.containsKey("error")) {
                log.error("语音识别接口报错: {}", responseJson.getByPath("error.message"));
                throw new RuntimeException("语音识别服务异常");
            }

            String recognizedText = responseJson.getStr("text");
            if (recognizedText == null || recognizedText.trim().isEmpty()) {
                recognizedText = "（未能识别出语音内容）";
            }

            return VoiceToTextVO.builder()
                    .text(recognizedText)
                    .confidence(0.99)
                    .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .build();

        } catch (Exception e) {
            log.error("语音识别处理失败", e);
            throw new RuntimeException("语音识别失败: " + e.getMessage());
        } finally {
            if (voiceFile != null && voiceFile.exists()) {
                FileUtil.del(voiceFile);
                log.info("临时语音文件已清理");
            }
        }
    }
}