package com.example.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import org.slf4j.MDC;

import java.util.Optional;

/**
 * 响应实体类封装
 * 严格对齐《开放 API 设计规范》第六章错误码表
 */
public record RestBean<T> (int code, String msg, T data, String requestId) {

    // --- 200: 操作成功 ---
    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(200, "操作成功", data, getRequestIdFromMDC());
    }

    public static <T> RestBean<T> success() {
        return success(null);
    }

    // --- 400: 参数错误 ---
    public static <T> RestBean<T> badRequest(String message) {
        return failure(400, "参数错误: " + message);
    }

    // --- 401: 未授权 ---
    public static <T> RestBean<T> unauthorized(String message) {
        return failure(401, "未授权: " + message);
    }

    // --- 403: 签名验证失败 (文档重点) ---
    public static <T> RestBean<T> forbidden(String message) {
        return failure(403, "签名验证失败: " + message);
    }

    // --- 404: 资源不存在 ---
    public static <T> RestBean<T> notFound(String message) {
        return failure(404, "资源不存在: " + message);
    }

    // --- 408: 请求超时 ---
    public static <T> RestBean<T> timeout() {
        return failure(408, "请求超时");
    }

    // --- 500: 服务端错误 ---
    public static <T> RestBean<T> error(String message) {
        return failure(500, "服务端错误: " + message);
    }

    // --- 503: 服务暂不可用 ---
    public static <T> RestBean<T> serviceUnavailable() {
        return failure(503, "服务暂不可用，请稍后重试");
    }

    /**
     * 通用失败逻辑
     */
    public static <T> RestBean<T> failure(int code, String msg) {
        return new RestBean<>(code, msg, null, getRequestIdFromMDC());
    }

    public String asJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }

    /**
     * 从 MDC 中获取 requestId（用于 Controller 构造自定义响应）
     */
    public static String getRequestIdFromMDC() {
        return Optional.ofNullable(MDC.get("reqId")).orElse("req000000000000");
    }
}