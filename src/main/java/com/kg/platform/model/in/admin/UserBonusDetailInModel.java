package com.kg.platform.model.in.admin;

import java.math.BigDecimal;
import java.util.Date;

public class UserBonusDetailInModel {

    private Long bonusDetailId;

    private Long userId;

    private Long extraBonusId;

    private BigDecimal kgAmount;

    private Date createTime;

    private BigDecimal tvAmount;

    private Long tvAccountFlowId;

    private Long kgAccountFlowId;

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

    public BigDecimal getKgAmount() {
        return kgAmount;
    }

    public void setKgAmount(BigDecimal kgAmount) {
        this.kgAmount = kgAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getTvAmount() {
        return tvAmount;
    }

    public void setTvAmount(BigDecimal tvAmount) {
        this.tvAmount = tvAmount;
    }

    public Long getTvAccountFlowId() {
        return tvAccountFlowId;
    }

    public void setTvAccountFlowId(Long tvAccountFlowId) {
        this.tvAccountFlowId = tvAccountFlowId;
    }

    public Long getKgAccountFlowId() {
        return kgAccountFlowId;
    }

    public void setKgAccountFlowId(Long kgAccountFlowId) {
        this.kgAccountFlowId = kgAccountFlowId;
    }

}