package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 用户角色枚举
 */
public enum UserRoleEnum {

    /**
     * 普通用户
     */
    NORMAL(1, "普通用户"),
    /**
     * 个人
     */
    PERSONAL(2, "个人"),
    /**
     * 媒体
     */
    MEDIA(3, "媒体"),
    /**
     * 企业
     */
    ENTERPRICE(4, "企业"),
    /**
     * 其他组织
     */
    OTHER(5, "其他组织");

    private int role;

    private String display;

    UserRoleEnum(int role, String display) {
        this.role = role;
        this.display = display;
    }

    public int getRole() {
        return role;
    }

    public String getDisplay() {
        return display;
    }

    public static UserRoleEnum getUserRoleEnum(int role) {
        UserRoleEnum roles[] = UserRoleEnum.values();
        Optional<UserRoleEnum> optional = Arrays.stream(roles).filter(item -> item.role == role).findFirst();
        return optional.isPresent() ? optional.get() : UserRoleEnum.NORMAL;
    }
}
