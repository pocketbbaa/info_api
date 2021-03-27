package com.kg.platform.model.request;

import java.io.Serializable;
import java.util.Date;

import com.aliyuncs.jaq.model.v20161123.AfsCheckRequest;

/**
 * 前台用户入参
 *
 * @author think
 */
public class UserkgRequest extends AfsCheckRequest implements Serializable {

    /**
     *
     */

    private static final long serialVersionUID = 9029186834138237452L;

    private String call_method;

    private Long userId;

    private String openId;

    private String userName;

    private String userEmail;

    private String userMobile;

    private String userSource;

    private String mobileArea;

    private String userPassword;

    private Integer userRole;

    private Integer userLevel;

    private Integer auditStatus;

    private Integer lockStatus;

    private Integer activityId;

    private Integer openType;

    private String thirdPartyId;

    private String sourceType;

    private Date createDate;

    private String registerIp;

    private Boolean emailAuthed;

    private Boolean mobileAuthed;

    private Integer userOrder;

    private Boolean hotUser;

    private String confirmPassword;

    private Integer apply_role;

    private String inmagelink;

    private String code;

    private String codeOld;

    private String verIfy;

    private String newPwd;

    private String valiDation;

    private String inviteCode;

    private String avatar;

    private Integer registerOrigin;

    private String userIp;

    private Integer osVersion;

    private String channel;

    public Integer getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(Integer osVersion) {
        this.osVersion = osVersion;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getCodeOld() {
        return codeOld;
    }

    public void setCodeOld(String codeOld) {
        this.codeOld = codeOld;
    }

    public Integer getRegisterOrigin() {
        return registerOrigin;
    }

    public void setRegisterOrigin(Integer registerOrigin) {
        this.registerOrigin = registerOrigin;
    }

    private String startTime;

    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
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
     * @return the valiDation
     */
    public String getValiDation() {
        return valiDation;
    }

    /**
     * @param valiDation
     *            the valiDation to set
     */
    public void setValiDation(String valiDation) {
        this.valiDation = valiDation;
    }

    /**
     * @return the newPwd
     */
    public String getNewPwd() {
        return newPwd;
    }

    /**
     * @param newPwd
     *            the newPwd to set
     */
    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    /**
     * @return the verIfy
     */
    public String getVerIfy() {
        return verIfy;
    }

    /**
     * @param verIfy
     *            the verIfy to set
     */
    public void setVerIfy(String verIfy) {
        this.verIfy = verIfy;
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
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
     * @return the apply_role
     */
    public Integer getApply_role() {
        return apply_role;
    }

    /**
     * @param apply_role
     *            the apply_role to set
     */
    public void setApply_role(Integer apply_role) {
        this.apply_role = apply_role;
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

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    public UserkgRequest(String call_method, Long userId, String userName, String userEmail, String userMobile,
            String mobileArea, String userPassword, Integer userRole, Integer userLevel, Integer auditStatus,
            Integer lockStatus, String thirdPartyId, Date createDate, String registerIp, Boolean emailAuthed,
            Boolean mobileAuthed, Integer userOrder, Boolean hotUser, String confirmPassword, Integer apply_role,
            String inmagelink, String code, String verIfy, String newPwd) {
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
        this.apply_role = apply_role;
        this.inmagelink = inmagelink;
        this.code = code;
        this.verIfy = verIfy;
        this.newPwd = newPwd;
    }

    public UserkgRequest() {
        super();
    }

}