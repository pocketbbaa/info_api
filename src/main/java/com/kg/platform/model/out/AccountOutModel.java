package com.kg.platform.model.out;

import java.math.BigDecimal;
import java.util.Date;

public class AccountOutModel {
    private Long accountId;

    private Long userId;

    private BigDecimal balance = BigDecimal.ZERO;

    private BigDecimal frozenBalance = BigDecimal.ZERO;

    private BigDecimal txbBalance = BigDecimal.ZERO;

    private BigDecimal txbFrozenBalance = BigDecimal.ZERO;

    private BigDecimal ritFrozenBalance = BigDecimal.ZERO;

    private BigDecimal ritBalance = BigDecimal.ZERO;

    private Integer status;

    private String txAddress;

    private Date createDate;

    private String txPassword;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(BigDecimal frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    public BigDecimal getTxbBalance() {
        return txbBalance;
    }

    public void setTxbBalance(BigDecimal txbBalance) {
        this.txbBalance = txbBalance;
    }

    public BigDecimal getTxbFrozenBalance() {
        return txbFrozenBalance;
    }

    public void setTxbFrozenBalance(BigDecimal txbFrozenBalance) {
        this.txbFrozenBalance = txbFrozenBalance;
    }

    public BigDecimal getRitFrozenBalance() {
        return ritFrozenBalance;
    }

    public void setRitFrozenBalance(BigDecimal ritFrozenBalance) {
        this.ritFrozenBalance = ritFrozenBalance;
    }

    public BigDecimal getRitBalance() {
        return ritBalance;
    }

    public void setRitBalance(BigDecimal ritBalance) {
        this.ritBalance = ritBalance;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTxAddress() {
        return txAddress;
    }

    public void setTxAddress(String txAddress) {
        this.txAddress = txAddress;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getTxPassword() {
        return txPassword;
    }

    public void setTxPassword(String txPassword) {
        this.txPassword = txPassword;
    }
}
