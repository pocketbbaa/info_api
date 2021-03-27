package com.kg.platform.model.in.admin;

import java.math.BigDecimal;

public class BonusInModel {

    private BigDecimal balance;

    private Long fromUserId;

    private Long flowId;

    private Long userId;

    private Long articleId;

    private Integer bussinssId;

    private Integer flowStatus;

    private String flowDetail;

    private String fromUserFlowDetail;

    private String modelType;

    //发放类型 1发放到余额  2 发放到冻结余额
    private Integer rewardType;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getBussinssId() {
        return bussinssId;
    }

    public void setBussinssId(Integer bussinssId) {
        this.bussinssId = bussinssId;
    }

    public Integer getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(Integer flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getFlowDetail() {
        return flowDetail;
    }

    public void setFlowDetail(String flowDetail) {
        this.flowDetail = flowDetail;
    }

    public String getFromUserFlowDetail() {
        return fromUserFlowDetail;
    }

    public void setFromUserFlowDetail(String fromUserFlowDetail) {
        this.fromUserFlowDetail = fromUserFlowDetail;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Integer getRewardType() {
        return rewardType;
    }

    public void setRewardType(Integer rewardType) {
        this.rewardType = rewardType;
    }
}