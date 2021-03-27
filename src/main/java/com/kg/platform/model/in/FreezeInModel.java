package com.kg.platform.model.in;

import java.math.BigDecimal;

public class FreezeInModel {

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 原因
     */
    private String cause;
    /**
     * 调用函数
     */
    private String callMethod;

    private String userId;
    /**
     * 类型 0 KG 1 RIT 2 TV
     */
    private Integer type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getCallMethod() {
        return callMethod;
    }

    public void setCallMethod(String callMethod) {
        this.callMethod = callMethod;
    }
}
