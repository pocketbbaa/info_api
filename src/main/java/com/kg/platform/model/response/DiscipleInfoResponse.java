package com.kg.platform.model.response;

import java.math.BigDecimal;

public class DiscipleInfoResponse {

    private String avatar;

    private String tId;

    private String userName;

    private BigDecimal todayIncome;

    private BigDecimal income;

    private String relDate;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getTodayIncome() {
        return todayIncome;
    }

    public void setTodayIncome(BigDecimal todayIncome) {
        this.todayIncome = todayIncome;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public String getRelDate() {
        return relDate;
    }

    public void setRelDate(String relDate) {
        this.relDate = relDate;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

}