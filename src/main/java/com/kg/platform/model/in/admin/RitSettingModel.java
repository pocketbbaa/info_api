package com.kg.platform.model.in.admin;

public class RitSettingModel {

    private Integer userType;

    private String ritRate;

    private String kgRate;

    private Long nextDayLimit;

    private Integer nextDayCnt;

    private String pushTimeHour;

    private String pushTimeMinute;

    private Long dayLimit;

    private Long monthLimit;

    private String rate;

    private Long minAmount;

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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Long getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Long minAmount) {
        this.minAmount = minAmount;
    }

    public String getPushTimeHour() {
        return pushTimeHour;
    }

    public void setPushTimeHour(String pushTimeHour) {
        this.pushTimeHour = pushTimeHour;
    }

    public String getPushTimeMinute() {
        return pushTimeMinute;
    }

    public void setPushTimeMinute(String pushTimeMinute) {
        this.pushTimeMinute = pushTimeMinute;
    }

    public Integer getUserType() {
        return userType;
    }

    public String getKgRate() {
        return kgRate;
    }

    public void setKgRate(String kgRate) {
        this.kgRate = kgRate;
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
}
