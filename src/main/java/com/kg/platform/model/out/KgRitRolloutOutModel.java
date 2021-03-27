package com.kg.platform.model.out;

import java.io.Serializable;

/**
 * kg_rit_rollout
 * @author 
 */
public class KgRitRolloutOutModel implements Serializable {
    private Long id;

    /**
     * 用户类型
     */
    private Integer userType;

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

}