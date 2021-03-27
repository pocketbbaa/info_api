package com.kg.platform.enumeration;


/**
 * 邀请奖励领取状态
 * 
 */
public enum RelBonusEnum {

    /**
     * 未领取
     */
    UNRECIEVED(0, "未领取"),

    /**
     * 已领取
     */
    RECIEVED(1, "已领取");

    private int type;

    private String display;

    RelBonusEnum(int type, String display) {
        this.type = type;
        this.display = display;
    }

    public int getType() {
        return type;
    }

    public String getDisplay() {
        return display;
    }

}
