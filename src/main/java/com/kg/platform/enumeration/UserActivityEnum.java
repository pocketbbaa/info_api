package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

public enum UserActivityEnum {

    NORMAL(0, "正常邀新"),
    ZHOU_CONCERT(1, "周杰伦演唱会活动"),
    WORLD_CUP(2, "世界杯活动");

    private int code;
    private String message;

    UserActivityEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static UserActivityEnum getByCode(int code) {
        UserActivityEnum roles[] = UserActivityEnum.values();
        Optional<UserActivityEnum> optional = Arrays.stream(roles).filter(item -> item.code == code).findFirst();
        return optional.orElse(UserActivityEnum.NORMAL);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
