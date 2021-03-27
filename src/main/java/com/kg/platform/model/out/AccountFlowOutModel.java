package com.kg.platform.model.out;

import java.math.BigDecimal;
import java.util.Date;

public class AccountFlowOutModel {

    private Integer actionTimes;// 最大数量

    private Integer actionMinTimes;// 最小数量

    private String typename;

    private Long accountFlowId;

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

    private Date flowDate;

    private String flowDetail;

    private String remark;

    private BigDecimal Income = BigDecimal.ZERO;// 累积所得

    private BigDecimal TodayIncome = BigDecimal.ZERO;// 今日所得

    private BigDecimal Reduce = BigDecimal.ZERO;// 今日消耗

    private BigDecimal BeforeIncome = BigDecimal.ZERO;// 昨日所得

    private String recenflowDate; // 最近时间

    private BigDecimal awrad = BigDecimal.ZERO;// 我的奖励

    private String avatar;// 头像

    private String userName; // 用户名

    private String tUId; // 师傅id

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

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public Long getAccountFlowId() {
        return accountFlowId;
    }

    public void setAccountFlowId(Long accountFlowId) {
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

    public Date getFlowDate() {
        return flowDate;
    }

    public void setFlowDate(Date flowDate) {
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

    public BigDecimal getIncome() {
        return Income;
    }

    public void setIncome(BigDecimal income) {
        Income = income;
    }

    public BigDecimal getTodayIncome() {
        return TodayIncome;
    }

    public void setTodayIncome(BigDecimal todayIncome) {
        TodayIncome = todayIncome;
    }

    public BigDecimal getReduce() {
        return Reduce;
    }

    public void setReduce(BigDecimal reduce) {
        Reduce = reduce;
    }

    public BigDecimal getBeforeIncome() {
        return BeforeIncome;
    }

    public void setBeforeIncome(BigDecimal beforeIncome) {
        BeforeIncome = beforeIncome;
    }

    public String getRecenflowDate() {
        return recenflowDate;
    }

    public void setRecenflowDate(String recenflowDate) {
        this.recenflowDate = recenflowDate;
    }

    public BigDecimal getAwrad() {
        return awrad;
    }

    public void setAwrad(BigDecimal awrad) {
        this.awrad = awrad;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String gettUId() {
        return tUId;
    }

    public void settUId(String tUId) {
        this.tUId = tUId;
    }
}