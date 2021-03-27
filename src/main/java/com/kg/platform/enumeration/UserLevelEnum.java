package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 用户级别枚举
 */
public enum UserLevelEnum {

    /**
     * 初级
     */
    PRIMARY(1, "初级");

    private int level;

    private String display;

    UserLevelEnum(int level, String display) {
        this.level = level;
        this.display = display;
    }

    public int getLevel() {
        return level;
    }

    public String getDisplay() {
        return display;
    }

    public static UserLevelEnum getUserLevelEnum(int level) {
        UserLevelEnum levels[] = UserLevelEnum.values();
        Optional<UserLevelEnum> optional = Arrays.stream(levels).filter(item -> item.level == level).findFirst();
        return optional.isPresent() ? optional.get() : UserLevelEnum.PRIMARY;
    }
}
