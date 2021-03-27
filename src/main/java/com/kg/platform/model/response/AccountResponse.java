package com.kg.platform.model.response;

import com.kg.platform.model.request.AccountRequest;

import java.util.Date;

public class AccountResponse {
    private Long accountId;

    private Long userId;

    private String balance;

    private String frozenBalance;

    private Byte status;

    private String txAddress;

    private Date createDate;

    private String txPassword;

    private String Income;// 累积所得

    private String TodayIncome;// 今日所得

    private String Reduce;// 今日消耗

    private String BeforeIncome;// 昨日所得

    /**
     * @return the income
     */
    public String getIncome() {
        return Income;
    }

    /**
     * @param income
     *            the income to set
     */
    public void setIncome(String income) {
        Income = income;
    }

    /**
     * @return the todayIncome
     */
    public String getTodayIncome() {
        return TodayIncome;
    }

    /**
     * @param todayIncome
     *            the todayIncome to set
     */
    public void setTodayIncome(String todayIncome) {
        TodayIncome = todayIncome;
    }

    /**
     * @return the reduce
     */
    public String getReduce() {
        return Reduce;
    }

    /**
     * @param reduce
     *            the reduce to set
     */
    public void setReduce(String reduce) {
        Reduce = reduce;
    }

    /**
     * @return the beforeIncome
     */
    public String getBeforeIncome() {
        return BeforeIncome;
    }

    /**
     * @param beforeIncome
     *            the beforeIncome to set
     */
    public void setBeforeIncome(String beforeIncome) {
        BeforeIncome = beforeIncome;
    }

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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(String frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
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