package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * 注册来源
 */
public enum RegistOriginEnum {


    KG_IOS(1, "千氪iOS"),
    KG_ANDROID(2, "千氪安卓"),
    KG_WEB(3, "千氪Web"),
    KG_H5(4, "H5"),
    KG_COLUMN_WEB(5, "千氪专栏Web"),
    OTHTER(6, "其他"),
	M(7,"千氪M站");

    private int code;
    private String message;

    RegistOriginEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static RegistOriginEnum getByCode(int code) {
        RegistOriginEnum roles[] = RegistOriginEnum.values();
        Optional<RegistOriginEnum> optional = Arrays.stream(roles).filter(item -> item.code == code).findFirst();
        return optional.orElse(RegistOriginEnum.OTHTER);
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
