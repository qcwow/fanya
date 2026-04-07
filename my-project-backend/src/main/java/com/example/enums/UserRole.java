package com.example.enums;

public enum UserRole {
    STUDENT(1, "学生"),
    TEACHER(2, "老师");
    private final Integer code;
    private final String desc;
    UserRole(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Integer getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}
