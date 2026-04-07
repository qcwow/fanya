package com.example.utils;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * API 签名工具类 - 适配超星泛雅开放 API 设计规范
 */
@Slf4j
public class SignUtils {

    /**
     * 核心逻辑：生成 MD5 签名
     * 严格遵循文档 1.4 规则：ASCII 升序排列 + key1value1 拼接 + staticKey + time
     */
    public static String generateSignature(Map<String, Object> params, String staticKey, String time) {
        // 1. 使用 TreeMap 自动进行 ASCII 升序排列
        TreeMap<String, Object> sortedMap = new TreeMap<>(params);
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Object> entry : sortedMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // 排除 enc 本身、time（作为后缀拼接）、以及空值
            if ("enc".equals(key) || "time".equals(key) || value == null) {
                continue;
            }

            // 过滤复杂类型（List、Map等），文档拼接通常只针对基本类型和字符串
            if (value instanceof Iterable || value instanceof Map) {
                continue;
            }

            String valueStr = value.toString();
            if (valueStr.isEmpty()) {
                continue;
            }

            // 拼接格式：key1value1key2value2...
            sb.append(key).append(valueStr);
        }

        // 2. 按照文档要求：最后拼接 staticKey 和 time
        sb.append(staticKey).append(time);

        String finalString = sb.toString();
        log.info("【签名校验】待签名字符串原始串: [{}]", finalString);

        // 3. MD5 加密并转为大写
        return DigestUtils.md5Hex(finalString).toUpperCase();
    }

    /**
     * 【推荐使用】全自动校验方法
     * 逻辑：自动识别 DTO 中的第一层参数和嵌套的 Info 对象参数，并进行比对
     *
     * @param dto        请求的 DTO 对象 (如 SyncCourseDTO 或 SyncUserDTO)
     * @param staticKey  从配置文件读取的密钥
     */
    public static void verifyPlatformSignature(Object dto, String staticKey) {
        // 1. 利用 Hutool 将 DTO 转为 Map
        Map<String, Object> allParams = BeanUtil.beanToMap(dto, false, true);

        // 2. 提取必要字段
        String providedEnc = (String) allParams.get("enc");
        String time = (String) allParams.get("time");

        if (providedEnc == null || time == null) {
            throw new RuntimeException("请求参数缺失：enc 或 time 不能为空");
        }

        // 3. 关键：打平嵌套对象 (Flatten)
        // 自动识别类似 courseInfo, userInfo 这样的 Map 字段，并将其内部属性提到顶层参与签名
        Map<String, Object> signMap = new TreeMap<>();
        allParams.forEach((k, v) -> {
            if (v instanceof Map) {
                // 如果是嵌套的对象，将其内部所有字段放入签名 Map
                signMap.putAll((Map<String, Object>) v);
            } else {
                signMap.put(k, v);
            }
        });

        // 4. 计算签名并比对
        String calculatedEnc = generateSignature(signMap, staticKey, time);

        log.info("【签名校验】客户端传入 enc: {}", providedEnc);
        log.info("【签名校验】系统计算出 enc: {}", calculatedEnc);

        if (!calculatedEnc.equalsIgnoreCase(providedEnc)) {
            throw new RuntimeException("签名验证失败，系统期望值: " + calculatedEnc);
        }
    }
}