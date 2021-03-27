package com.kg.platform.model.out;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户出参
 * 
 * @author think
 *
 */
public class UserkgOutModel implements Serializable {

    /**
     * 
     */

    private static final long serialVersionUID = 9029186834138237452L;

    private String applyRole;

    private String call_method;

    private String userId;

    private String userName;

    private String userEmail;

    private String userMobile;

    private String mobileArea;

    private String userPassword;

    private Integer userRole;

    private Byte userLevel;

    private int auditStatus;

    private Integer lockStatus;

    private String thirdPartyId;

    private Date createDate;

    private String registerIp;

    private Boolean emailAuthed;

    private Boolean mobileAuthed;

    private Integer userOrder;

    private Boolean hotUser;

    private String confirmPassword;

    private long articlesum;

    private long realnameAuthed;

    private long tradepasswordSet;

    private long depositStatus;

    private Date lockDate;

    private String lockTime;

    private Integer lockUnit;

    private String refuseReason;// 实名申请不通过原因

    private String certificateStatus;// 实名申请状态

    private String inviteCode;

    private Integer inviteStatus;

    private Integer bonusStatus;

    private String bonusFreezeReason;

    private String avatar;

    private Integer registerOrigin;

    private Integer columnAuthed; //专栏是否认证 0 未认证 1 已认证

    private Integer sortCnt = 0;

    private Integer isHotUser;

    private Integer registChannel;

    private Integer rankingList;

    private BigDecimal txbBalance;

    private BigDecimal ritBalance;

    private BigDecimal ritFrozenBalance;

    private Integer columnLevel; //专栏等级

    public Integer getColumnLevel() {
        return columnLevel;
    }

    public void setColumnLevel(Integer columnLevel) {
        this.columnLevel = columnLevel;
    }

    public BigDecimal getTxbBalance() {
        return txbBalance;
    }

    public void setTxbBalance(BigDecimal txbBalance) {
        this.txbBalance = txbBalance;
    }

    public BigDecimal getRitBalance() {
        return ritBalance;
    }

    public void setRitBalance(BigDecimal ritBalance) {
        this.ritBalance = ritBalance;
    }

    public BigDecimal getRitFrozenBalance() {
        return ritFrozenBalance;
    }

    public void setRitFrozenBalance(BigDecimal ritFrozenBalance) {
        this.ritFrozenBalance = ritFrozenBalance;
    }

    public Integer getRankingList() {
        return rankingList;
    }

    public void setRankingList(Integer rankingList) {
        this.rankingList = rankingList;
    }

    public Integer getRegistChannel() {
        return registChannel;
    }

    public void setRegistChannel(Integer registChannel) {
        this.registChannel = registChannel;
    }

    public Integer getIsHotUser() {
        return isHotUser;
    }

    public void setIsHotUser(Integer isHotUser) {
        this.isHotUser = isHotUser;
    }

    public Integer getSortCnt() {
        return sortCnt;
    }

    public void setSortCnt(Integer sortCnt) {
        this.sortCnt = sortCnt;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Integer getInviteStatus() {
        return inviteStatus;
    }

    public void setInviteStatus(Integer inviteStatus) {
        this.inviteStatus = inviteStatus;
    }

    public Integer getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(Integer bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    public String getBonusFreezeReason() {
        return bonusFreezeReason;
    }

    public void setBonusFreezeReason(String bonusFreezeReason) {
        this.bonusFreezeReason = bonusFreezeReason;
    }

    /**
     * @return the certificateStatus
     */
    public String getCertificateStatus() {
        return certificateStatus;
    }

    /**
     * @param certificateStatus
     *            the certificateStatus to set
     */
    public void setCertificateStatus(String certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public Date getLockDate() {
        return lockDate;
    }

    public void setLockDate(Date lockDate) {
        this.lockDate = lockDate;
    }

    public String getLockTime() {
        return lockTime;
    }

    public void setLockTime(String lockTime) {
        this.lockTime = lockTime;
    }

    public Integer getLockUnit() {
        return lockUnit;
    }

    public void setLockUnit(Integer lockUnit) {
        this.lockUnit = lockUnit;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    /**
     * @return the realnameAuthed
     */
    public long getRealnameAuthed() {
        return realnameAuthed;
    }

    /**
     * @param realnameAuthed
     *            the realnameAuthed to set
     */
    public void setRealnameAuthed(long realnameAuthed) {
        this.realnameAuthed = realnameAuthed;
    }

    /**
     * @return the tradepasswordSet
     */
    public long getTradepasswordSet() {
        return tradepasswordSet;
    }

    /**
     * @param tradepasswordSet
     *            the tradepasswordSet to set
     */
    public void setTradepasswordSet(long tradepasswordSet) {
        this.tradepasswordSet = tradepasswordSet;
    }

    /**
     * @return the depositStatus
     */
    public long getDepositStatus() {
        return depositStatus;
    }

    /**
     * @param depositStatus
     *            the depositStatus to set
     */
    public void setDepositStatus(long depositStatus) {
        this.depositStatus = depositStatus;
    }

    /**
     * @return the articlesum
     */
    public long getArticlesum() {
        return articlesum;
    }

    /**
     * @param articlesum
     *            the articlesum to set
     */
    public void setArticlesum(long articlesum) {
        this.articlesum = articlesum;
    }

    /**
     * @return the applyRole
     */
    public String getApplyRole() {
        return applyRole;
    }

    /**
     * @param applyRole
     *            the applyRole to set
     */
    public void setApplyRole(String applyRole) {
        this.applyRole = applyRole;
    }

    /**
     * @return the call_method
     */
    public String getCall_method() {
        return call_method;
    }

    /**
     * @param call_method
     *            the call_method to set
     */
    public void setCall_method(String call_method) {
        this.call_method = call_method;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the userEmail
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * @param userEmail
     *            the userEmail to set
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @return the userMobile
     */
    public String getUserMobile() {
        return userMobile;
    }

    /**
     * @param userMobile
     *            the userMobile to set
     */
    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    /**
     * @return the mobileArea
     */
    public String getMobileArea() {
        return mobileArea;
    }

    /**
     * @param mobileArea
     *            the mobileArea to set
     */
    public void setMobileArea(String mobileArea) {
        this.mobileArea = mobileArea;
    }

    /**
     * @return the userPassword
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * @param userPassword
     *            the userPassword to set
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /**
     * @return the confirmPassword
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * @param confirmPassword
     *            the confirmPassword to set
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the userRole
     */
    public Integer getUserRole() {
        return userRole;
    }

    /**
     * @param userRole
     *            the userRole to set
     */
    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    /**
     * @return the userLevel
     */
    public Byte getUserLevel() {
        return userLevel;
    }

    /**
     * @param userLevel
     *            the userLevel to set
     */
    public void setUserLevel(Byte userLevel) {
        this.userLevel = userLevel;
    }

    /**
     * @return the auditStatus
     */
    public int getAuditStatus() {
        return auditStatus;
    }

    /**
     * @param auditStatus
     *            the auditStatus to set
     */
    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    /**
     * @return the lockStatus
     */
    public Integer getLockStatus() {
        return lockStatus;
    }

    /**
     * @param lockStatus
     *            the lockStatus to set
     */
    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }

    /**
     * @return the thirdPartyId
     */
    public String getThirdPartyId() {
        return thirdPartyId;
    }

    /**
     * @param thirdPartyId
     *            the thirdPartyId to set
     */
    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     *            the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the registerIp
     */
    public String getRegisterIp() {
        return registerIp;
    }

    /**
     * @param registerIp
     *            the registerIp to set
     */
    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    /**
     * @return the emailAuthed
     */
    public Boolean getEmailAuthed() {
        return emailAuthed;
    }

    /**
     * @param emailAuthed
     *            the emailAuthed to set
     */
    public void setEmailAuthed(Boolean emailAuthed) {
        this.emailAuthed = emailAuthed;
    }

    /**
     * @return the mobileAuthed
     */
    public Boolean getMobileAuthed() {
        return mobileAuthed;
    }

    /**
     * @param mobileAuthed
     *            the mobileAuthed to set
     */
    public void setMobileAuthed(Boolean mobileAuthed) {
        this.mobileAuthed = mobileAuthed;
    }

    /**
     * @return the userOrder
     */
    public Integer getUserOrder() {
        return userOrder;
    }

    /**
     * @param userOrder
     *            the userOrder to set
     */
    public void setUserOrder(Integer userOrder) {
        this.userOrder = userOrder;
    }

    /**
     * @return the hotUser
     */
    public Boolean getHotUser() {
        return hotUser;
    }

    /**
     * @param hotUser
     *            the hotUser to set
     */
    public void setHotUser(Boolean hotUser) {
        this.hotUser = hotUser;
    }

    public UserkgOutModel(String call_method, String userId, String userName, String userEmail, String userMobile,
            String mobileArea, String userPassword, Integer userRole, Byte userLevel, int auditStatus,
            Integer lockStatus, String thirdPartyId, Date createDate, String registerIp, Boolean emailAuthed,
            Boolean mobileAuthed, Integer userOrder, Boolean hotUser, String confirmPassword) {
        super();
        this.call_method = call_method;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userMobile = userMobile;
        this.mobileArea = mobileArea;
        this.userPassword = userPassword;
        this.userRole = userRole;
        this.userLevel = userLevel;
        this.auditStatus = auditStatus;
        this.lockStatus = lockStatus;
        this.thirdPartyId = thirdPartyId;
        this.createDate = createDate;
        this.registerIp = registerIp;
        this.emailAuthed = emailAuthed;
        this.mobileAuthed = mobileAuthed;
        this.userOrder = userOrder;
        this.hotUser = hotUser;
        this.confirmPassword = confirmPassword;
    }

    public UserkgOutModel() {
        super();
    }

}