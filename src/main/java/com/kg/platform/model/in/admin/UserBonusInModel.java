package com.kg.platform.model.in.admin;

import java.math.BigDecimal;
import java.util.Date;

public class UserBonusInModel {

    private BigDecimal totalTvAmount;

    private BigDecimal totalKgAmount;

    private Long extraBonusId;

    private Integer sysUserId;

    private Integer totalNum;

    private Date createTime;

    private String bonusReason;

    private Integer rewardType;

    public BigDecimal getTotalTvAmount() {
        return totalTvAmount;
    }

    public void setTotalTvAmount(BigDecimal totalTvAmount) {
        this.totalTvAmount = totalTvAmount;
    }

    public BigDecimal getTotalKgAmount() {
        return totalKgAmount;
    }

    public void setTotalKgAmount(BigDecimal totalKgAmount) {
        this.totalKgAmount = totalKgAmount;
    }

    public Long getExtraBonusId() {
        return extraBonusId;
    }

    public void setExtraBonusId(Long extraBonusId) {
        this.extraBonusId = extraBonusId;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBonusReason() {
        return bonusReason;
    }

    public void setBonusReason(String bonusReason) {
        this.bonusReason = bonusReason;
    }

    public Integer getRewardType() {
        return rewardType;
    }

    public void setRewardType(Integer rewardType) {
        this.rewardType = rewardType;
    }
}