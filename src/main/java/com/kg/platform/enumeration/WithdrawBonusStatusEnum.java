package com.kg.platform.enumeration;

/**
 * 用户邀请是否需要审查
 * 提现状态 0审核中  1 已通过 2 已撤销
 */
public enum WithdrawBonusStatusEnum {

    /**
     * 需要审查
     */
    AUDITING(0, "审核中"),
    
    /**
     * 通过
     */
    PASS(1, "已通过"),

    /**
     * 不需要审查
     */
    CANCEL(2, "已撤销");

    private int status;
    
    private String display;

    WithdrawBonusStatusEnum(int status, String display) {
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
