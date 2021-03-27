package com.kg.platform.dao.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AccountDepositFlow {
    private Long depositFlowId;

    private Long accountFlowId;

    private Long userId;

    private String userPhone;

    private BigDecimal depositAmount;

    private BigDecimal accountAmount;

    private Date rechargeTime;

    private Date accountTime;

    private String fromAddress;

    private String toAddress;

    private String txId;

    private String remark;

    public Long getAccountFlowId() {
        return accountFlowId;
    }

    public void setAccountFlowId(Long accountFlowId) {
        this.accountFlowId = accountFlowId;
    }

    public Long getDepositFlowId() {
        return depositFlowId;
    }

    public void setDepositFlowId(Long depositFlowId) {
        this.depositFlowId = depositFlowId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    public Date getRechargeTime() {
        return rechargeTime;
    }

    public void setRechargeTime(Date rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    public Date getAccountTime() {
        return accountTime;
    }

    public void setAccountTime(Date accountTime) {
        this.accountTime = accountTime;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}