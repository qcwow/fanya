package com.example.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

/**
 * 签名生成工具 - 用于测试Progress模块接口
 */
public class SignatureGeneratorHelper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // 从 application-dev.yml 中读取的 staticKey
    private static final String STATIC_KEY = "edu_platform_secret_key_2024";

    /**
     * 生成进度追踪接口的签名示例
     */
    public static void generateTrackSignature() {
        System.out.println("========== 学习进度追踪接口签名示例 ==========");
        
        // 1. 准备请求参数（不包含 enc）
        Map<String, Object> params = new TreeMap<>();
        params.put("schoolId", "sch10001");
        params.put("userId", "stu20001");
        params.put("courseId", "cou30001");
        params.put("lessonId", "lesson20240520001");
        params.put("currentSectionId", "sec002");
        params.put("progressPercent", 60.5f);
        params.put("lastOperateTime", "2024-05-20 10:10:00");
        params.put("qaRecordId", "ans20240520001");
        
        // 2. 设置当前时间
        String time = LocalDateTime.now().format(FORMATTER);
        params.put("time", time);
        
        System.out.println("请求时间: " + time);
        System.out.println("\n请求参数:");
        params.forEach((k, v) -> System.out.println("  " + k + ": " + v));
        
        // 3. 生成签名
        String enc = SignUtils.generateSignature(params, STATIC_KEY, time);
        
        System.out.println("\n生成的签名 (enc): " + enc);
        System.out.println("\n完整请求体:");
        System.out.println("{");
        params.forEach((k, v) -> {
            if (v instanceof String) {
                System.out.println("  \"" + k + "\": \"" + v + "\",");
            } else {
                System.out.println("  \"" + k + "\": " + v + ",");
            }
        });
        System.out.println("  \"enc\": \"" + enc + "\"");
        System.out.println("}");
        System.out.println("==============================================\n");
    }

    /**
     * 生成节奏调整接口的签名示例
     */
    public static void generateAdjustSignature() {
        System.out.println("========== 学习节奏调整接口签名示例 ==========");
        
        // 1. 准备请求参数（不包含 enc）
        Map<String, Object> params = new TreeMap<>();
        params.put("userId", "stu20001");
        params.put("lessonId", "lesson20240520001");
        params.put("currentSectionId", "sec002");
        params.put("understandingLevel", "partial");
        params.put("qaRecordId", "ans20240520001");
        
        // 2. 设置当前时间
        String time = LocalDateTime.now().format(FORMATTER);
        params.put("time", time);
        
        System.out.println("请求时间: " + time);
        System.out.println("\n请求参数:");
        params.forEach((k, v) -> System.out.println("  " + k + ": " + v));
        
        // 3. 生成签名
        String enc = SignUtils.generateSignature(params, STATIC_KEY, time);
        
        System.out.println("\n生成的签名 (enc): " + enc);
        System.out.println("\n完整请求体:");
        System.out.println("{");
        params.forEach((k, v) -> {
            if (v instanceof String) {
                System.out.println("  \"" + k + "\": \"" + v + "\",");
            } else {
                System.out.println("  \"" + k + "\": " + v + ",");
            }
        });
        System.out.println("  \"enc\": \"" + enc + "\"");
        System.out.println("}");
        System.out.println("==============================================\n");
    }

    public static void main(String[] args) {
        generateTrackSignature();
        generateAdjustSignature();
    }
}
