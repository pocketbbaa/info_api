package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 系统消息 0
 * 奖励消息 1
 * 动态消息 2
 * 快讯更新 3
 * 热点资讯 4
 */
public enum PushClassifyEnum {

    SYSTEM_MESSAGE(0, "系统消息"),
    REWARD_MESSAGE(1, "奖励消息"),
    DYNAMIC_MESSAGE(2, "动态消息"),
    FLASH_UPDATE_MESSAGE(3, "快讯更新"),
    HOT_ARTICLE_MESSAGE(4, "热点资讯");

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


    PushClassifyEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public static PushClassifyEnum getByCode(int code) {
        PushClassifyEnum roles[] = PushClassifyEnum.values();
        Optional<PushClassifyEnum> optional = Arrays.stream(roles).filter(item -> item.code == code).findFirst();
        return optional.orElse(PushClassifyEnum.SYSTEM_MESSAGE);
    }

}
