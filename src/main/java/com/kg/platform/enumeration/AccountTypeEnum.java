package com.kg.platform.enumeration;

public enum AccountTypeEnum {

    TZ(1, "钛值"),
    TXB(2, "钛小白");

    private int status;

    private String display;

    AccountTypeEnum(int status, String display) {
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
