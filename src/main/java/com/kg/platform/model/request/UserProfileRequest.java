package com.kg.platform.model.request;

import java.io.Serializable;

public class UserProfileRequest implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6087927852022584206L;

    private String auditStatus;

    private String code;

    private String call_method;

    private String profilename;// 专栏名称

    private String profileavatar;// 专栏头像

    private Long userId;

    private Long articleId;

    private Integer roleId;

    private String avatar;

    private String sex;

    private String country;

    private String province;

    private String city;

    private String county;

    private String resume;

    private String roleDefine;

    private String columnName;

    private String columnIntro;

    private String columnAvatar;

    private String columnCountry;

    private String columnProvince;

    private String columnCity;

    private String columnCounty;

    private String realName;

    private String idcard;

    private String idcardPic;

    private String idcardFront;

    private String idcardBack;

    private String email;

    private String mobile;

    private String otherPic;

    private String companyName;

    private String licensePic;

    private String siteLink;

    private String username;

    private String depositAddress;

    private String userStatus;

    private String applyRole;

    private Long loginUserId; // 当前登录用户ID

	private String port;//M站端口

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
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
     * @return the userStatus
     */
    public String getUserStatus() {
        return userStatus;
    }

    /**
     * @param userStatus
     *            the userStatus to set
     */
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
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
     * @return the idcardBack
     */
    public String getIdcardBack() {
        return idcardBack;
    }

    /**
     * @param idcardBack
     *            the idcardBack to set
     */
    public void setIdcardBack(String idcardBack) {
        this.idcardBack = idcardBack;
    }

    /**
     * @return the auditStatus
     */
    public String getAuditStatus() {
        return auditStatus;
    }

    /**
     * @param auditStatus
     *            the auditStatus to set
     */
    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getRoleDefine() {
        return roleDefine;
    }

    public void setRoleDefine(String roleDefine) {
        this.roleDefine = roleDefine;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnIntro() {
        return columnIntro;
    }

    public void setColumnIntro(String columnIntro) {
        this.columnIntro = columnIntro;
    }

    public String getColumnAvatar() {
        return columnAvatar;
    }

    public void setColumnAvatar(String columnAvatar) {
        this.columnAvatar = columnAvatar;
    }

    public String getColumnCountry() {
        return columnCountry;
    }

    public void setColumnCountry(String columnCountry) {
        this.columnCountry = columnCountry;
    }

    public String getColumnProvince() {
        return columnProvince;
    }

    public void setColumnProvince(String columnProvince) {
        this.columnProvince = columnProvince;
    }

    public String getColumnCity() {
        return columnCity;
    }

    public void setColumnCity(String columnCity) {
        this.columnCity = columnCity;
    }

    public String getColumnCounty() {
        return columnCounty;
    }

    public void setColumnCounty(String columnCounty) {
        this.columnCounty = columnCounty;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIdcardPic() {
        return idcardPic;
    }

    public void setIdcardPic(String idcardPic) {
        this.idcardPic = idcardPic;
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

    public String getOtherPic() {
        return otherPic;
    }

    public void setOtherPic(String otherPic) {
        this.otherPic = otherPic;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLicensePic() {
        return licensePic;
    }

    public void setLicensePic(String licensePic) {
        this.licensePic = licensePic;
    }

    public String getSiteLink() {
        return siteLink;
    }

    public void setSiteLink(String siteLink) {
        this.siteLink = siteLink;
    }

    /**
     * @return the profilename
     */
    public String getProfilename() {
        return profilename;
    }

    /**
     * @param profilename
     *            the profilename to set
     */
    public void setProfilename(String profilename) {
        this.profilename = profilename;
    }

    /**
     * @return the profileavatar
     */
    public String getProfileavatar() {
        return profileavatar;
    }

    /**
     * @param profileavatar
     *            the profileavatar to set
     */
    public void setProfileavatar(String profileavatar) {
        this.profileavatar = profileavatar;
    }

    public UserProfileRequest() {
        super();
    }

    public Long getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(Long loginUserId) {
        this.loginUserId = loginUserId;
    }

}