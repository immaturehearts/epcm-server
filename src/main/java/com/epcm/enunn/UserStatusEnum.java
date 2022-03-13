package com.epcm.enunn;

public enum UserStatusEnum {
    AVAILABLE(0,"可用"),
    FORBIDDEN(1,"封禁");

    Integer code;
    String description;

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    UserStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
