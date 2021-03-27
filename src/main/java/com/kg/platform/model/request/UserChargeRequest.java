package com.kg.platform.model.request;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class UserChargeRequest implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4238495171877146247L;

    @NotNull(message = "交易ID不能为空")
    private String txId;

    /**
     * 对方钱包地址（转出地址）
     */
    private String fromAddress;

    /**
     * 本地钱包地址（转入地址）
     */
    private String toAddress;

    @NotNull(message = "用户标识不能为空")
    private String userId;

    /**
     * 充值状态 0 充值中 1 充值成功
     */
    @NotNull(message = "充值状态不能为空")
    private Integer status;

    /**
     * 充值金额
     */
    private BigDecimal rechargeAmount;

    /**
     * 到账金额
     */
    private BigDecimal accountAmount;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

}