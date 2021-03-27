package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

public enum PushPlaceEnum {

    ALL(0, "手机+内部"),
    APP(1, "内部"),
    PHOINE(2, "手机");
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


    PushPlaceEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public static PushPlaceEnum getByCode(int code) {
        PushPlaceEnum roles[] = PushPlaceEnum.values();
        Optional<PushPlaceEnum> optional = Arrays.stream(roles).filter(item -> item.code == code).findFirst();
        return optional.orElse(PushPlaceEnum.ALL);
    }

}
