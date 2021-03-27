package com.kg.platform.enumeration;

/**
 * Created by Administrator on 2018/4/3.
 */
public enum RegisterOriginEnum {

    IOS(1, "IOS"),
    ANDROID(2, "ANDROID"),
    WEB(3, "WEB"),
    H5(4, "H5"),
    AUTHOR(5, "作者端"),
    OTHER(6, "其他"),
    M(7,"千氪M站");

    private Integer origin;

    private String desc;

    RegisterOriginEnum(int origin, String desc) {
        this.origin = origin;
        this.desc = desc;
    }

    public Integer getOrigin() {
        return origin;
    }

    public String getDesc() {
        return desc;
    }
}
