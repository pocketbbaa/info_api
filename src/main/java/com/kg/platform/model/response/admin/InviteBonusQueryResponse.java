package com.kg.platform.model.response.admin;

import java.util.Date;

/**
 * 邀新奖励查询出参
 */
public class InviteBonusQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = 4389132702591588908L;

    private String userId;

    private String userName;

    private String mobile;

    private Integer userRole;

    private String userRoleDisplay;

    private Integer level;

    private String levelDisplay;

    private Integer inviteCount;

    private Integer bonusStatus;

    private Integer inviteStatus;

    private Double getAmount;

    private Double freezeAmount;

    private Date bonusWithdrawDate;

    public Double getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(Double freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Integer getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(Integer inviteCount) {
        this.inviteCount = inviteCount;
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

    public Double getGetAmount() {
        return getAmount;
    }

    public void setGetAmount(Double getAmount) {
        this.getAmount = getAmount;
    }

    public Date getBonusWithdrawDate() {
        return bonusWithdrawDate;
    }

    public void setBonusWithdrawDate(Date bonusWithdrawDate) {
        this.bonusWithdrawDate = bonusWithdrawDate;
    }

}
