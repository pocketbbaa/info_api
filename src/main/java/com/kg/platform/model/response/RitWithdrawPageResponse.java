package com.kg.platform.model.response;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/9/6.
 */
public class RitWithdrawPageResponse {

    private Float rate;//手续费率

    private String minAmount;//最低转出

    /**
     * 每日兑换限制 额度
     */
    private String dayLimit;

    /**
     * 每月可兑换额度
     */
    private String monthLimit;

    private String todayOut;//今日已转出

    private String monthOut;//当月已转出

    private String balance;//余额

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(String dayLimit) {
        this.dayLimit = dayLimit;
    }

    public String getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(String monthLimit) {
        this.monthLimit = monthLimit;
    }

    public String getTodayOut() {
        return todayOut;
    }

    public void setTodayOut(String todayOut) {
        this.todayOut = todayOut;
    }

    public String getMonthOut() {
        return monthOut;
    }

    public void setMonthOut(String monthOut) {
        this.monthOut = monthOut;
    }
}
