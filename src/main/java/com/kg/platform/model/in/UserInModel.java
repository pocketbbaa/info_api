package com.kg.platform.model.in;

import java.util.Date;

public class UserInModel {

    private Long userId;

    private String userName;

    private String openId;

    private String userEmail;

    private String userMobile;

    private String mobileArea;

    private String userPassword;

    private String userSource;

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

    private String tradepasswordSet;

    private String inviteCode;

    private Integer registerOrigin;

    private Integer columnAuthed; //专栏是否认证 0 未认证 1 已认证

    private Integer start;

    private Integer limit;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getColumnAuthed() {
        return columnAuthed;
    }

    public void setColumnAuthed(Integer columnAuthed) {
        this.columnAuthed = columnAuthed;
    }

    public Integer getRegisterOrigin() {
        return registerOrigin;
    }

    public void setRegisterOrigin(Integer registerOrigin) {
        this.registerOrigin = registerOrigin;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getUserSource() {
        return userSource;
    }

    public void setUserSource(String userSource) {
        this.userSource = userSource;
    }

    /**
     * @return the tradepasswordSet
     */
    public String getTradepasswordSet() {
        return tradepasswordSet;
    }

    /**
     * @param tradepasswordSet
     *            the tradepasswordSet to set
     */
    public void setTradepasswordSet(String tradepasswordSet) {
        this.tradepasswordSet = tradepasswordSet;
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

    /**
     * @return the applyRole
     */
    public Integer getApplyRole() {
        return applyRole;
    }

    /**
     * @param applyRole
     *            the applyRole to set
     */
    public void setApplyRole(Integer applyRole) {
        this.applyRole = applyRole;
    }

    public UserInModel(String userEmail, String userMobile) {
        super();
        this.userEmail = userEmail;
        this.userMobile = userMobile;
    }

    public UserInModel() {
        super();
    }

}