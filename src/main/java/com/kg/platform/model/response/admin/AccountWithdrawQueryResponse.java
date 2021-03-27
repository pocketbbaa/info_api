package com.kg.platform.model.response.admin;

import java.util.Date;

/**
 * 提币管理查询出参
 */
public class AccountWithdrawQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -5495777700817784301L;

    private String flowId;

    private String email;

    private String mobile;

    private String toAddress;

    private Double withdrawAmount;

    private Double accountAmount;

    private Date withdrawTime;

    private Date accountTime;

    private Integer status;

    private String statusDisplay;

    private Integer userRole;

    private String userRoleDisplay;

    private Integer level;

    private String levelDisplay;

    private String userId;

    private Double poundageAmount;

    private String remark;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
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

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public Double getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(Double withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public Double getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(Double accountAmount) {
        this.accountAmount = accountAmount;
    }

    public Date getWithdrawTime() {
        return withdrawTime;
    }

    public void setWithdrawTime(Date withdrawTime) {
        this.withdrawTime = withdrawTime;
    }

    public Date getAccountTime() {
        return accountTime;
    }

    public void setAccountTime(Date accountTime) {
        this.accountTime = accountTime;
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

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getUserRoleDisplay() {
        return userRoleDisplay;
    }

    public void setUserRoleDisplay(String userRoleDisplay) {
        this.userRoleDisplay = userRoleDisplay;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLevelDisplay() {
        return levelDisplay;
    }

    public void setLevelDisplay(String levelDisplay) {
        this.levelDisplay = levelDisplay;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getPoundageAmount() {
        return poundageAmount;
    }

    public void setPoundageAmount(Double poundageAmount) {
        this.poundageAmount = poundageAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
