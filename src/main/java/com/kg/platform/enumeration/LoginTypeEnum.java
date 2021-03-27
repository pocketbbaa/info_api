package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 审核状态枚举
 */
public enum LoginTypeEnum {

    /**
     * 手机验证码登录
     */
    MOBILE_LOGIN(1, "手机验证码登录"),
    /**
     * 用户名密码登录
     */
    USERPASSWORD_LOGIN(2, "用户名密码登录");

    private int status;

    private String display;

    LoginTypeEnum(int status, String display) {
        this.status = status;
        this.display = display;
    }

    public int getStatus() {
        return status;
    }

    public String getDisplay() {
        return display;
    }

    public static LoginTypeEnum getAuditStatusEnum(int status) {
        LoginTypeEnum statuses[] = LoginTypeEnum.values();
        Optional<LoginTypeEnum> optional = Arrays.stream(statuses).filter(item -> item.status == status).findFirst();
        return optional.isPresent() ? optional.get() : LoginTypeEnum.MOBILE_LOGIN;
    }
}
