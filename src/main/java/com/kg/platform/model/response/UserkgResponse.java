package com.kg.platform.model.response;

import java.io.Serializable;
import java.util.Date;

import com.kg.platform.model.TokenModel;

/**
 * 
 * @author think
 *
 */
public class UserkgResponse implements Serializable{

    private String applyRole;

    private String inmagelink;

    private String userId;

    private String userName;

    private String userEmail;

    private String userMobile;

    private String mobileArea;

    private String userPassword;

    private Integer userRole;

    private Byte userLevel;

    private int auditStatus;

    private String bindStatus;

    private Integer lockStatus;

    // 0其他站点 1本站
    private Integer accountFrom;

    private String thirdPartyId;

    private Date createDate;

    private String applyColumnTime;

    private String registerIp;

    private Boolean emailAuthed;

    private Boolean mobileAuthed;

    private Integer bindWeibo;

    private Integer bindWeiXin;

    private Integer userOrder;

    private Boolean hotUser;

    private String token;

    TokenModel tokenModel;

    private boolean state;

    private String mobIle;

    private long articlesum;

    private long realnameAuthed;

    private long tradepasswordSet;

    private long depositStatus;

    private String userStatus;

    private String wbName;

    private String wxName;

    private Date lockDate;

    private String lockTime;

    private Integer lockUnit;

    private UserkgResponse response;

    private String refuseReason;

    private String certRefuseReason;

    private String depositAddress;

    private String atskMobile;

    private String realName;// 真实姓名

    private String idcardNo;// 身份证号码

    private String idcardFront;

    private String certificateStatus;// 实名申请状态

    private String depositAmount;// 保证金额度

    private String remainingAmount;// 剩余金额

    private Integer loginBonusStatus; // 是否领取过登录奖励

    private String inviteCode;

    private Integer inviteStatus;

    private Integer bonusStatus;

    private String bonusFreezeReason;

    private String avatar;

    private String headImgurl;

    private String nickName;

    private String openId;

    private Integer newUser; // 是否新用户 0 不是 1是

    private String identityTag; //身份标识

    private int realAuthedTag; //实名标签 0:无，1：有

    private int vipTag; //是否有大V标签 0：无，1：有

    public String getIdentityTag() {
        return identityTag;
    }

    public void setIdentityTag(String identityTag) {
        this.identityTag = identityTag;
    }

    public int getRealAuthedTag() {
        return realAuthedTag;
    }

    public void setRealAuthedTag(int realAuthedTag) {
        this.realAuthedTag = realAuthedTag;
    }

    public int getVipTag() {
        return vipTag;
    }

    public void setVipTag(int vipTag) {
        this.vipTag = vipTag;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getNewUser() {
        return newUser;
    }

    public void setNewUser(Integer newUser) {
        this.newUser = newUser;
    }

    public String getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(String bindStatus) {
        this.bindStatus = bindStatus;
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

    public Integer getLoginBonusStatus() {
        return loginBonusStatus;
    }

    public void setLoginBonusStatus(Integer loginBonusStatus) {
        this.loginBonusStatus = loginBonusStatus;
    }

    /**
     * @return the remainingAmount
     */
    public String getRemainingAmount() {
        return remainingAmount;
    }

    /**
     * @param remainingAmount
     *            the remainingAmount to set
     */
    public void setRemainingAmount(String remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    /**
     * @return the depositAmount
     */
    public String getDepositAmount() {
        return depositAmount;
    }

    /**
     * @param depositAmount
     *            the depositAmount to set
     */
    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
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

    /**
     * @return the idcardFront
     */
    public String getIdcardFront() {
        return idcardFront;
    }

    /**
     * @param idcardFront
     *            the idcardFront to set
     */
    public void setIdcardFront(String idcardFront) {
        this.idcardFront = idcardFront;
    }

    /**
     * @return the realName
     */
    public String getRealName() {
        return realName;
    }

    /**
     * @param realName
     *            the realName to set
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * @return the idcardNo
     */
    public String getIdcardNo() {
        return idcardNo;
    }

    /**
     * @param idcardNo
     *            the idcardNo to set
     */
    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    /**
     * @return the atskMobile
     */
    public String getAtskMobile() {
        return atskMobile;
    }

    /**
     * @param atskMobile
     *            the atskMobile to set
     */
    public void setAtskMobile(String atskMobile) {
        this.atskMobile = atskMobile;
    }

    /**
     * @return the depositAddress
     */
    public String getDepositAddress() {
        return depositAddress;
    }

    /**
     * @param depositAddress
     *            the depositAddress to set
     */
    public void setDepositAddress(String depositAddress) {
        this.depositAddress = depositAddress;
    }

    /**
     * @return the refuseReason
     */
    public String getRefuseReason() {
        return refuseReason;
    }

    /**
     * @param refuseReason
     *            the refuseReason to set
     */
    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getCertRefuseReason() {
        return certRefuseReason;
    }

    public void setCertRefuseReason(String certRefuseReason) {
        this.certRefuseReason = certRefuseReason;
    }

    /**
     * @return the response
     */
    public UserkgResponse getResponse() {
        return response;
    }

    /**
     * @param response
     *            the response to set
     */
    public void setResponse(UserkgResponse response) {
        this.response = response;
    }

    /**
     * @return the userStatus
     */
    public String getUserStatus() {
        return userStatus;
    }

    /**
     * @param string
     *            the userStatus to set
     */
    public void setUserStatus(String string) {
        this.userStatus = string;
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
     * @return the mobIle
     */
    public String getMobIle() {
        return mobIle;
    }

    /**
     * @param mobIle
     *            the mobIle to set
     */
    public void setMobIle(String mobIle) {
        this.mobIle = mobIle;
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

    public Byte getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Byte userLevel) {
        this.userLevel = userLevel;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

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
     * @return the lockDate
     */
    public Date getLockDate() {
        return lockDate;
    }

    /**
     * @param lockDate
     *            the lockDate to set
     */
    public void setLockDate(Date lockDate) {
        this.lockDate = lockDate;
    }

    /**
     * @return the lockTime
     */
    public String getLockTime() {
        return lockTime;
    }

    /**
     * @param lockTime
     *            the lockTime to set
     */
    public void setLockTime(String lockTime) {
        this.lockTime = lockTime;
    }

    /**
     * @return the lockUnit
     */
    public Integer getLockUnit() {
        return lockUnit;
    }

    /**
     * @param lockUnit
     *            the lockUnit to set
     */
    public void setLockUnit(Integer lockUnit) {
        this.lockUnit = lockUnit;
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

    public String getApplyColumnTime() {
        return applyColumnTime;
    }

    public void setApplyColumnTime(String applyColumnTime) {
        this.applyColumnTime = applyColumnTime;
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
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token
     *            the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the tokenModel
     */
    public TokenModel getTokenModel() {
        return tokenModel;
    }

    /**
     * @param tokenModel
     *            the tokenModel to set
     */
    public void setTokenModel(TokenModel tokenModel) {
        this.tokenModel = tokenModel;
    }

    /**
     * @return the state
     */
    public boolean isState() {
        return state;
    }

    /**
     * @param state
     *            the state to set
     */
    public void setState(boolean state) {
        this.state = state;
    }

    /**
     * @return the inmagelink
     */
    public String getInmagelink() {
        return inmagelink;
    }

    /**
     * @param inmagelink
     *            the inmagelink to set
     */
    public void setInmagelink(String inmagelink) {
        this.inmagelink = inmagelink;
    }

    public String getHeadImgurl() {
        return headImgurl;
    }

    public void setHeadImgurl(String headImgurl) {
        this.headImgurl = headImgurl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getBindWeibo() {
        return bindWeibo;
    }

    public void setBindWeibo(Integer bindWeibo) {
        this.bindWeibo = bindWeibo;
    }

    public Integer getBindWeiXin() {
        return bindWeiXin;
    }

    public void setBindWeiXin(Integer bindWeiXin) {
        this.bindWeiXin = bindWeiXin;
    }

    public String getWbName() {
        return wbName;
    }

    public void setWbName(String wbName) {
        this.wbName = wbName;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public Integer getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Integer accountFrom) {
        this.accountFrom = accountFrom;
    }

}