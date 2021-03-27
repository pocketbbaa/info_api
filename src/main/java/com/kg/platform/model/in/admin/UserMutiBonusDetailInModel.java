package com.kg.platform.model.in.admin;

import java.math.BigDecimal;
import java.util.Date;

public class UserMutiBonusDetailInModel {

    private Long bonusDetailId;

    private Long userId;

    private Long extraBonusId;

    private BigDecimal amount;

    private Integer coinType;

    private Long accountFlowId;

    private Date createTime;

    private Integer rewardTo;

    public Long getBonusDetailId() {
        return bonusDetailId;
    }

    public void setBonusDetailId(Long bonusDetailId) {
        this.bonusDetailId = bonusDetailId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getExtraBonusId() {
        return extraBonusId;
    }

    public void setExtraBonusId(Long extraBonusId) {
        this.extraBonusId = extraBonusId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getCoinType() {
        return coinType;
    }

    public void setCoinType(Integer coinType) {
        this.coinType = coinType;
    }

    public Long getAccountFlowId() {
        return accountFlowId;
    }

    public void setAccountFlowId(Long accountFlowId) {
        this.accountFlowId = accountFlowId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getRewardTo() {
        return rewardTo;
    }

    public void setRewardTo(Integer rewardTo) {
        this.rewardTo = rewardTo;
    }
}