package com.kg.platform.model.request.admin;

import java.util.Date;

/**
 * 奖励管理入参
 */
public class BonusQueryRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = -3856391458987099549L;

    private String userId;

    private Date startDate;

    private Date endDate;

    private Double minAmount;

    private Double maxAmount;

    private String userName;

    private String mobile;

    private Integer userRole;

    private Integer userLevel;

    private Integer bonusStatus;

    private Integer inviteStatus;

    private Integer businessTypeId;

    private Integer minValue;

    private Integer maxValue;

    public Integer getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Integer businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Integer getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(Integer bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    public Integer getInviteStatus() {
        return inviteStatus;
    }

    public void setInviteStatus(Integer inviteStatus) {
        this.inviteStatus = inviteStatus;
    }

    public Integer getMinValue() {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

}
