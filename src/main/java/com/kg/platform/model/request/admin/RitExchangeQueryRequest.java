package com.kg.platform.model.request.admin;

/**
 * RIT兑换查询model
 */
public class RitExchangeQueryRequest extends AdminBaseRequest {

    private String userName;
    private String userPhone;
    private Integer userRole;
    private String ritAmountmMin;
    private String ritAmountMax;
    private String kgAmountmMin;
    private String kgAmountMax;
    private String exchangeStartTime;
    private String exchangeEndTime;

    public String getExchangeStartTime() {
        return exchangeStartTime;
    }

    public void setExchangeStartTime(String exchangeStartTime) {
        this.exchangeStartTime = exchangeStartTime;
    }

    public String getExchangeEndTime() {
        return exchangeEndTime;
    }

    public void setExchangeEndTime(String exchangeEndTime) {
        this.exchangeEndTime = exchangeEndTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getRitAmountmMin() {
        return ritAmountmMin;
    }

    public void setRitAmountmMin(String ritAmountmMin) {
        this.ritAmountmMin = ritAmountmMin;
    }

    public String getRitAmountMax() {
        return ritAmountMax;
    }

    public void setRitAmountMax(String ritAmountMax) {
        this.ritAmountMax = ritAmountMax;
    }

    public String getKgAmountmMin() {
        return kgAmountmMin;
    }

    public void setKgAmountmMin(String kgAmountmMin) {
        this.kgAmountmMin = kgAmountmMin;
    }

    public String getKgAmountMax() {
        return kgAmountMax;
    }

    public void setKgAmountMax(String kgAmountMax) {
        this.kgAmountMax = kgAmountMax;
    }
}
