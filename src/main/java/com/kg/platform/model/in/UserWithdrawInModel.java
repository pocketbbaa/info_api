package com.kg.platform.model.in;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserWithdrawInModel implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 3115070521289149529L;

    private Long withdrawFlowId;

    private Long userId;

    private BigDecimal withdrawAmount;

    private BigDecimal poundateAmount;

    private String txAddress;

    public Long getWithdrawFlowId() {
        return withdrawFlowId;
    }

    public void setWithdrawFlowId(Long withdrawFlowId) {
        this.withdrawFlowId = withdrawFlowId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTxAddress() {
        return txAddress;
    }

    public void setTxAddress(String txAddress) {
        this.txAddress = txAddress;
    }

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public BigDecimal getPoundateAmount() {
        return poundateAmount;
    }

    public void setPoundateAmount(BigDecimal poundateAmount) {
        this.poundateAmount = poundateAmount;
    }

}