package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

public enum CommonSettingEnum {

    RIT_EXCHANGE_BUTTON(1, "show_rit_exchange_button"),
    RIT_ROLLOUT_BUTTON(2, "show_rit_rollout_button");

    private int code;
    private String settingKey;

    CommonSettingEnum(int code, String settingKey) {
        this.code = code;
        this.settingKey = settingKey;
    }

    public static CommonSettingEnum getByCode(int code) {
        CommonSettingEnum roles[] = CommonSettingEnum.values();
        Optional<CommonSettingEnum> optional = Arrays.stream(roles).filter(item -> item.code == code).findFirst();
        return optional.orElse(CommonSettingEnum.RIT_EXCHANGE_BUTTON);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }
}
