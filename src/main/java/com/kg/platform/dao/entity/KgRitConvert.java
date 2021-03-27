package com.kg.platform.dao.entity;

import java.io.Serializable;

/**
 * kg_rit_convert
 * @author 
 */
public class KgRitConvert implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户类型 0 普通用户 1 专栏用户
     */
    private Integer userType;

    /**
     * rit于氪金的汇率
     */
    private String ritRate;

    /**
     * 每日兑换限制 额度
     */
    private Long dayLimit;

    /**
     * 待更新每日兑换额度
     */
    private Long nextDayLimit;

    /**
     * 待更新每次兑换次数
     */
    private Integer nextDayCnt;

    /**
     * 每日兑换次数
     */
    private Integer dayCnt;

    /**
     * 每日刷新额度时间
     */
    private String pushTime;

    /**
     * 次日额度刷新时间
     */
    private String nextPushTime;

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

    public String getRitRate() {
        return ritRate;
    }

    public void setRitRate(String ritRate) {
        this.ritRate = ritRate;
    }

    public Long getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(Long dayLimit) {
        this.dayLimit = dayLimit;
    }

    public Long getNextDayLimit() {
        return nextDayLimit;
    }

    public void setNextDayLimit(Long nextDayLimit) {
        this.nextDayLimit = nextDayLimit;
    }

    public Integer getNextDayCnt() {
        return nextDayCnt;
    }

    public void setNextDayCnt(Integer nextDayCnt) {
        this.nextDayCnt = nextDayCnt;
    }

    public Integer getDayCnt() {
        return dayCnt;
    }

    public void setDayCnt(Integer dayCnt) {
        this.dayCnt = dayCnt;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public String getNextPushTime() {
        return nextPushTime;
    }

    public void setNextPushTime(String nextPushTime) {
        this.nextPushTime = nextPushTime;
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
        KgRitConvert other = (KgRitConvert) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserType() == null ? other.getUserType() == null : this.getUserType().equals(other.getUserType()))
            && (this.getRitRate() == null ? other.getRitRate() == null : this.getRitRate().equals(other.getRitRate()))
            && (this.getDayLimit() == null ? other.getDayLimit() == null : this.getDayLimit().equals(other.getDayLimit()))
            && (this.getNextDayLimit() == null ? other.getNextDayLimit() == null : this.getNextDayLimit().equals(other.getNextDayLimit()))
            && (this.getNextDayCnt() == null ? other.getNextDayCnt() == null : this.getNextDayCnt().equals(other.getNextDayCnt()))
            && (this.getPushTime() == null ? other.getPushTime() == null : this.getPushTime().equals(other.getPushTime()))
            && (this.getNextPushTime() == null ? other.getNextPushTime() == null : this.getNextPushTime().equals(other.getNextPushTime()));
    }
}