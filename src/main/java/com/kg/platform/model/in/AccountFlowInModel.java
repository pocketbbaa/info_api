package com.kg.platform.model.in;

import java.math.BigDecimal;
import java.util.Date;

public class AccountFlowInModel {
    private int limit;

    private int start;

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

    private BigDecimal poundageAmount;

    private Date accountDate;

    private Long articleId;

    private Long rewardUid;

    private Integer bonusTotalPerson;

    private Integer flowStatus;

    private Date flowDate;

    private String flowDetail;

    private String remark;

    private String startTime;

    private String endTime;

    private String Type;

    private String authAction;// 对应操作ID

    /**
     * @return the authAction
     */
    public String getAuthAction() {
        return authAction;
    }

    /**
     * @param authAction
     *            the authAction to set
     */
    public void setAuthAction(String authAction) {
        this.authAction = authAction;
    }

    /**
     * @return the type
     */
    public String getType() {
        return Type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        Type = type;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    /**
     * @return the limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * @param limit
     *            the limit to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start
     *            the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }

    public Long getRewardUid() {
        return rewardUid;
    }

    public void setRewardUid(Long rewardUid) {
        this.rewardUid = rewardUid;
    }

}