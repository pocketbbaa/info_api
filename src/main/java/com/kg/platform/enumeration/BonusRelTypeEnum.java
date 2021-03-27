package com.kg.platform.enumeration;


/**
 * UserRealition关联类型
 */
public enum BonusRelTypeEnum {
    /**
     * 邀新
     */
    INVITE(1, "邀新"),
    /**
     * 首次点赞文章
     */
    BINDING(2, "手动绑定");
   
    private int type;

    private String display;

    BonusRelTypeEnum(int type, String display) {
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
