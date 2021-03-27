package com.kg.platform.enumeration;

public enum NewsFlashTypeEnum {

    ALLKX(-1,"全部"),
    QKLKX(0, "区块链快讯"),
    JRKX(1, "金融快讯"),
    GSKX(2, "股市快讯");

    private int status;

    private String display;

    NewsFlashTypeEnum(int status, String display) {
        this.status = status;
        this.display = display;
    }

    public int getStatus() {
        return status;
    }

    public String getDisplay() {
        return display;
    }
}
