package com.kg.platform.model.response;

import java.math.BigDecimal;
import java.util.Date;

public class AccountFlowResponse {

    private BigDecimal balance;

    private String typename;

    private String typeName;

    private String accountFlowId;

    private Long relationFlowId;

    private Long userId;

    private String userPhone;

    private String userEmail;

    private BigDecimal amount;

    private Integer businessTypeId;

    private String txId;

    private String txAddress;

    private BigDecimal accountAmount;

    private BigDecimal freezeAmount;

    private BigDecimal poundageAmount;

    private Date accountDate;

    private Long articleId;

    private Integer bonusTotalPerson;

    private Integer flowStatus;

    private String freezeAmountString;

    private String flowDate;

    private String flowDetail;

    private String remark;

    private String Incomes;// 累积所得

    private String TodayIncome;// 今日所得

    private String Reduce;// 今日消耗

    private String BeforeIncome;// 昨日所得

    private Integer actionTimes;// 最大数量

    private Integer actionMinTimes;// 最小数量

    private String mobileArea;

    private String userMobile;// 用户手机

    private String withdrawFlowId;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getAccountFlowId() {
        return accountFlowId;
    }

    public void setAccountFlowId(String accountFlowId) {
        this.accountFlowId = accountFlowId;
    }

    public Long getRelationFlowId() {
        return relationFlowId;
    }

    public void setRelationFlowId(Long relationFlowId) {
        this.relationFlowId = relationFlowId;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Integer businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getTxAddress() {
        return txAddress;
    }

    public void setTxAddress(String txAddress) {
        this.txAddress = txAddress;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    public BigDecimal getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public BigDecimal getPoundageAmount() {
        return poundageAmount;
    }

    public void setPoundageAmount(BigDecimal poundageAmount) {
        this.poundageAmount = poundageAmount;
    }

    public Date getAccountDate() {
        return accountDate;
    }

    public void setAccountDate(Date accountDate) {
        this.accountDate = accountDate;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Integer getBonusTotalPerson() {
        return bonusTotalPerson;
    }

    public void setBonusTotalPerson(Integer bonusTotalPerson) {
        this.bonusTotalPerson = bonusTotalPerson;
    }

    public Integer getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(Integer flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getFreezeAmountString() {
        return freezeAmountString;
    }

    public void setFreezeAmountString(String freezeAmountString) {
        this.freezeAmountString = freezeAmountString;
    }

    public String getFlowDate() {
        return flowDate;
    }

    public void setFlowDate(String flowDate) {
        this.flowDate = flowDate;
    }

    public String getFlowDetail() {
        return flowDetail;
    }

    public void setFlowDetail(String flowDetail) {
        this.flowDetail = flowDetail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIncomes() {
        return Incomes;
    }

    public void setIncomes(String incomes) {
        Incomes = incomes;
    }

    public String getTodayIncome() {
        return TodayIncome;
    }

    public void setTodayIncome(String todayIncome) {
        TodayIncome = todayIncome;
    }

    public String getReduce() {
        return Reduce;
    }

    public void setReduce(String reduce) {
        Reduce = reduce;
    }

    public String getBeforeIncome() {
        return BeforeIncome;
    }

    public void setBeforeIncome(String beforeIncome) {
        BeforeIncome = beforeIncome;
    }

    public Integer getActionTimes() {
        return actionTimes;
    }

    public void setActionTimes(Integer actionTimes) {
        this.actionTimes = actionTimes;
    }

    public Integer getActionMinTimes() {
        return actionMinTimes;
    }

    public void setActionMinTimes(Integer actionMinTimes) {
        this.actionMinTimes = actionMinTimes;
    }

    public String getMobileArea() {
        return mobileArea;
    }

    public void setMobileArea(String mobileArea) {
        this.mobileArea = mobileArea;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getWithdrawFlowId() {
        return withdrawFlowId;
    }

    public void setWithdrawFlowId(String withdrawFlowId) {
        this.withdrawFlowId = withdrawFlowId;
    }
}