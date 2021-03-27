package com.kg.platform.dao.entity;

import java.io.Serializable;

/**
 * kg_rit_rollout
 * @author 
 */
public class KgRitRollout implements Serializable {
    private Long id;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 氪金对于rit的兑换率
     */
    private String kgRate;

    /**
     * 每日兑换限制 额度
     */
    private Long dayLimit;

    /**
     * 每月可兑换额度
     */
    private Long monthLimit;

    /**
     * 汇率
     */
    private Float rate;

    /**
     * 最低转出
     */
    private Long minAmount;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getKgRate() {
        return kgRate;
    }

    public void setKgRate(String kgRate) {
        this.kgRate = kgRate;
    }

    public Long getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(Long dayLimit) {
        this.dayLimit = dayLimit;
    }

    public Long getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(Long monthLimit) {
        this.monthLimit = monthLimit;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Long getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Long minAmount) {
        this.minAmount = minAmount;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        KgRitRollout other = (KgRitRollout) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserType() == null ? other.getUserType() == null : this.getUserType().equals(other.getUserType()))
            && (this.getKgRate() == null ? other.getKgRate() == null : this.getKgRate().equals(other.getKgRate()))
            && (this.getDayLimit() == null ? other.getDayLimit() == null : this.getDayLimit().equals(other.getDayLimit()))
            && (this.getMonthLimit() == null ? other.getMonthLimit() == null : this.getMonthLimit().equals(other.getMonthLimit()))
            && (this.getRate() == null ? other.getRate() == null : this.getRate().equals(other.getRate()))
            && (this.getMinAmount() == null ? other.getMinAmount() == null : this.getMinAmount().equals(other.getMinAmount()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserType() == null) ? 0 : getUserType().hashCode());
        result = prime * result + ((getKgRate() == null) ? 0 : getKgRate().hashCode());
        result = prime * result + ((getDayLimit() == null) ? 0 : getDayLimit().hashCode());
        result = prime * result + ((getMonthLimit() == null) ? 0 : getMonthLimit().hashCode());
        result = prime * result + ((getRate() == null) ? 0 : getRate().hashCode());
        result = prime * result + ((getMinAmount() == null) ? 0 : getMinAmount().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userType=").append(userType);
        sb.append(", kgRate=").append(kgRate);
        sb.append(", dayLimit=").append(dayLimit);
        sb.append(", monthLimit=").append(monthLimit);
        sb.append(", rate=").append(rate);
        sb.append(", minAmount=").append(minAmount);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}