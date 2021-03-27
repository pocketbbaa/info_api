package com.kg.platform.model.response;

/**
 * Created by Administrator on 2018/9/14.
 */
public class KgUserResponse {

    private String userId;

    private String userMobile;

    private Integer userRole;

    private String txbBalance;

    private String ritBalance;

    private String ritFrozenBalance;

    private String userRoleDisplay;

    private String fee;//手续费

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getTxbBalance() {
        return txbBalance;
    }

    public void setTxbBalance(String txbBalance) {
        this.txbBalance = txbBalance;
    }

    public String getRitBalance() {
        return ritBalance;
    }

    public void setRitBalance(String ritBalance) {
        this.ritBalance = ritBalance;
    }

    public String getRitFrozenBalance() {
        return ritFrozenBalance;
    }

    public void setRitFrozenBalance(String ritFrozenBalance) {
        this.ritFrozenBalance = ritFrozenBalance;
    }

    public String getUserRoleDisplay() {
        return userRoleDisplay;
    }

    public void setUserRoleDisplay(String userRoleDisplay) {
        this.userRoleDisplay = userRoleDisplay;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
