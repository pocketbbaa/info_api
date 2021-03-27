package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 推送全部用户 1
 * 推送指定用户 2
 * 绑定别名 3
 * 解绑别名 4
 * 绑定tag  5
 * 解绑tag  6
 */
public enum PushTypeEnum {

    PUSH_ALL(1, "推送全部用户"),
    PUSH_SPECIFIED(2, "推送指定用户"),
    BINDING_ALIAS(3, "绑定别名"),
    UNBINDING_ALIAS(4, "解绑别名"),
    BINDING_TAG(5, "绑定tag"),
    UNBINDING_TAG(6, "解绑tag");

    private int code;
    private String type;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    PushTypeEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public static PushTypeEnum getByCode(int code) {
        PushTypeEnum roles[] = PushTypeEnum.values();
        Optional<PushTypeEnum> optional = Arrays.stream(roles).filter(item -> item.code == code).findFirst();
        return optional.orElse(PushTypeEnum.PUSH_ALL);
    }

}
