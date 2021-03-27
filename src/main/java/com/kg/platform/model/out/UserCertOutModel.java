package com.kg.platform.model.out;

/**
 * 实名认证查询入参
 */
public class UserCertOutModel {

    private String userId;

    private String email;

    private String mobile;

    private Integer status;

    private String realName;

    private String idcardNo;

    private String idcardFront;

    private String certRefuseReason;

    public String getCertRefuseReason() {
        return certRefuseReason;
    }

    public void setCertRefuseReason(String certRefuseReason) {
        this.certRefuseReason = certRefuseReason;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
