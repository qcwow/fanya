package com.example.utils;

import org.apache.commons.codec.digest.DigestUtils; // 或者用 Spring 的方案
import java.util.Map;
import java.util.TreeMap;

public class SignUtils {
    public static String generateSignature(Map<String, Object> params, String staticKey, String time) {
        // 1. 按 key ASCII 升序排列
        TreeMap<String, Object> sortedMap = new TreeMap<>(params);
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Object> entry : sortedMap.entrySet()) {
            // 排除 enc 本身和空值
            if (!"enc".equals(entry.getKey()) && entry.getValue() != null && !"".equals(entry.getValue().toString())) {
                sb.append(entry.getKey()).append(entry.getValue());
            }
        }

        // 2. 拼接 staticKey 和 time
        sb.append(staticKey).append(time);

        // 3. MD5 加密并强制转为大写（符合 PDF 示例）
        return DigestUtils.md5Hex(sb.toString()).toUpperCase();
    }
}