package com.kg.platform.dao.entity;

import java.util.Date;

public class User {
    private Long userId;

    private String userName;

    private String userEmail;

    private String userMobile;

    private String mobileArea;

    private String userPassword;

    private Integer userRole;

    private Integer userLevel;

    private Integer auditStatus;

    private Integer lockStatus;

    private String thirdPartyId;

    private Date createDate;

    private String registerIp;

    private Boolean emailAuthed;

    private Boolean mobileAuthed;

    private Integer userOrder;

    private Boolean hotUser;

    private Integer applyRole;

    private Boolean tradepasswordSet;

    private Integer registerOrigin;

    private Integer realnameAuthed;

    private String inviteCode;

    private Integer rankingList;

    public Integer getRankingList() {
        return rankingList;
    }

    public void setRankingList(Integer rankingList) {
        this.rankingList = rankingList;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Integer getRealnameAuthed() {
        return realnameAuthed;
    }

    public void setRealnameAuthed(Integer realnameAuthed) {
        this.realnameAuthed = realnameAuthed;
    }

    public Boolean getTradepasswordSet() {
        return tradepasswordSet;
    }

    public void setTradepasswordSet(Boolean tradepasswordSet) {
        this.tradepasswordSet = tradepasswordSet;
    }

    public Integer getApplyRole() {
        return applyRole;
    }

    public void setApplyRole(Integer applyRole) {
        this.applyRole = applyRole;
    }

    public Integer getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(Integer userOrder) {
        this.userOrder = userOrder;
    }

    public Boolean getHotUser() {
        return hotUser;
    }

    public void setHotUser(Boolean hotUser) {
        this.hotUser = hotUser;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getMobileArea() {
        return mobileArea;
    }

    public void setMobileArea(String mobileArea) {
        this.mobileArea = mobileArea;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
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

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public Boolean getEmailAuthed() {
        return emailAuthed;
    }

    public void setEmailAuthed(Boolean emailAuthed) {
        this.emailAuthed = emailAuthed;
    }

    public Boolean getMobileAuthed() {
        return mobileAuthed;
    }

    public void setMobileAuthed(Boolean mobileAuthed) {
        this.mobileAuthed = mobileAuthed;
    }

    public Integer getRegisterOrigin() {
        return registerOrigin;
    }

    public void setRegisterOrigin(Integer registerOrigin) {
        this.registerOrigin = registerOrigin;
    }

}