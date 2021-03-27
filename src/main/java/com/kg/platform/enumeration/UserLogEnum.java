package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 文章审核状态枚举
 */
public enum UserLogEnum {

    KG_USER_COLLECT(1, "kg_user_collect_log", "用户收藏日志"),
    KG_USER_LIKE(2, "kg_user_like_log", "用户点赞日志"),
    KG_USER_SHARE(3, "kg_user_share_log", "用户分享日志"),
    KG_USER_BROWSE(4, "kg_user_browse_log", "用户浏览日志");

    private int code;
    private String table;
    private String message;

    UserLogEnum(int code, String table, String message) {
        this.code = code;
        this.table = table;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static UserLogEnum getByCode(int code) {
        UserLogEnum statuses[] = UserLogEnum.values();
        Optional<UserLogEnum> optional = Arrays.stream(statuses).filter(item -> item.code == code)
                .findFirst();
        return optional.orElse(UserLogEnum.KG_USER_BROWSE);
    }
}
