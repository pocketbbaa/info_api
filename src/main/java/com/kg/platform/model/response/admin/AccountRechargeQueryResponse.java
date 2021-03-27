package com.kg.platform.model.response.admin;

import java.util.Date;

/**
 * 充值管理查询出参
 */
public class AccountRechargeQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -8774964020297688L;

    private String flowId;

    private String userId;

    private String email;

    private String mobile;

    private Double amount;

    private Date rechargeTime;

    private Date accountTime;

    private String remark;

    private Integer status;

    private String statusDisplay;

    private Integer userRole;

    private Integer level;

    private String toAddress;

    private String userRoleDisplay;

    private String levelDisplay;

    public String getUserRoleDisplay() {
        return userRoleDisplay;
    }

    public void setUserRoleDisplay(String userRoleDisplay) {
        this.userRoleDisplay = userRoleDisplay;
    }

    public String getLevelDisplay() {
        return levelDisplay;
    }

    public void setLevelDisplay(String levelDisplay) {
        this.levelDisplay = levelDisplay;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getRechargeTime() {
        return rechargeTime;
    }

    public void setRechargeTime(Date rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    public Date getAccountTime() {
        return accountTime;
    }

    public void setAccountTime(Date accountTime) {
        this.accountTime = accountTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }
}
