package com.example.utils;

import java.util.TreeMap;

/**
 * 签名生成工具（用于测试）
 */
public class SignatureGenerator {
    
    public static void main(String[] args) {
        String time = "2026-04-07 17:30:00";
        String staticKey = "edu_platform_secret_key_2024";
        
        // 示例1：生成课件解析接口的签名
        System.out.println("=== 1. 课件解析接口签名示例 ===");
        TreeMap<String, Object> parseParams = new TreeMap<>();
        parseParams.put("schoolId", "sch10001");
        parseParams.put("userId", "tea20001");
        parseParams.put("courseId", "cou30001");
        parseParams.put("fileType", "pdf");
        parseParams.put("fileUrl", "http://xxx.com/course/ppt/123.pdf");
        parseParams.put("isExtractKeyPoint", true);
        
        String enc1 = SignUtils.generateSignature(parseParams, staticKey, time);
        System.out.println("请求参数:");
        System.out.println("{");
        System.out.println("  \"schoolId\": \"sch10001\",");
        System.out.println("  \"userId\": \"tea20001\",");
        System.out.println("  \"courseId\": \"cou30001\",");
        System.out.println("  \"fileType\": \"pdf\",");
        System.out.println("  \"fileUrl\": \"http://xxx.com/course/ppt/123.pdf\",");
        System.out.println("  \"isExtractKeyPoint\": true,");
        System.out.println("  \"time\": \"" + time + "\",");
        System.out.println("  \"enc\": \"" + enc1 + "\"");
        System.out.println("}");
        System.out.println();
        
        // 示例2：生成脚本生成接口的签名
        System.out.println("=== 2. 脚本生成接口签名示例 ===");
        TreeMap<String, Object> scriptParams = new TreeMap<>();
        scriptParams.put("parseId", "parse1");
        scriptParams.put("teachingStyle", "standard");
        scriptParams.put("speechSpeed", "normal");
        scriptParams.put("customOpening", "同学们好，今天我们学习梁弯曲理论的核心知识点");
        
        String enc2 = SignUtils.generateSignature(scriptParams, staticKey, time);
        System.out.println("请求参数:");
        System.out.println("{");
        System.out.println("  \"parseId\": \"parse1\",");
        System.out.println("  \"teachingStyle\": \"standard\",");
        System.out.println("  \"speechSpeed\": \"normal\",");
        System.out.println("  \"customOpening\": \"同学们好，今天我们学习梁弯曲理论的核心知识点\",");
        System.out.println("  \"time\": \"" + time + "\",");
        System.out.println("  \"enc\": \"" + enc2 + "\"");
        System.out.println("}");
        System.out.println();
        
        // 示例3：生成语音合成接口的签名
        System.out.println("=== 3. 语音合成接口签名示例 ===");
        TreeMap<String, Object> audioParams = new TreeMap<>();
        audioParams.put("scriptId", "script1");
        audioParams.put("voiceType", "female_standard");
        audioParams.put("audioFormat", "mp3");
        // 注意：sectionIds 是数组，在签名时需要特殊处理
        // 这里简化处理，不包含 sectionIds
        
        String enc3 = SignUtils.generateSignature(audioParams, staticKey, time);
        System.out.println("请求参数（不含 sectionIds）:");
        System.out.println("{");
        System.out.println("  \"scriptId\": \"script1\",");
        System.out.println("  \"voiceType\": \"female_standard\",");
        System.out.println("  \"audioFormat\": \"mp3\",");
        System.out.println("  \"time\": \"" + time + "\",");
        System.out.println("  \"enc\": \"" + enc3 + "\"");
        System.out.println("}");
        System.out.println();
        
        System.out.println("========================================");
        System.out.println("提示：请复制上面的完整 JSON 到 HTTP 请求中");
        System.out.println("注意：time 需要与当前时间接近（5分钟内）");
    }
}
