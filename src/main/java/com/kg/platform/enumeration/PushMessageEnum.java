package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 推送枚举
 */
public enum PushMessageEnum {


    SYSTEM_TITLE(0, "系统通知", ""),

    GET_COMMENT(1, "获得评论", "{1}对你的文章《{2}》发表了评论：{3}"),
    GET_REPLY(2, "收到回复", "{1}对你在《{2}》下的评论进行了回复：{3}"),
    GET_AUTHOR_REPLY(3, "收到作者回复", "《{1}》的作者回复了你的评论：{2}");

    private int code;
    private String type;
    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    PushMessageEnum(int code, String type, String message) {
        this.code = code;
        this.type = type;
        this.message = message;
    }

    public static PushMessageEnum getByCode(int code) {
        PushMessageEnum roles[] = PushMessageEnum.values();
        Optional<PushMessageEnum> optional = Arrays.stream(roles).filter(item -> item.code == code).findFirst();
        return optional.orElse(PushMessageEnum.GET_COMMENT);
    }


}
