package com.kg.platform.dao.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * kg_user_certificated
 * @author 
 */
public class KgUserCertificated implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String idcardNo;

    /**
     * 身份证正面图片
     */
    private String idcardFront;

    /**
     * 身份证背面图片
     */
    private String idcardBack;

    /**
     * 手持身份证照片
     */
    private String idcardPic;

    /**
     * 认证状态 0 不通过 1 已认证 2 审核中
     */
    private Byte certificateStatus;

    /**
     * 审核不通过原因
     */
    private String refuseReason;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 审核时间
     */
    private Date auditDate;

    /**
     * 审核人
     */
    private Integer auditUser;

    private static final long serialVersionUID = 1L;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    public String getIdcardFront() {
        return idcardFront;
    }

    public void setIdcardFront(String idcardFront) {
        this.idcardFront = idcardFront;
    }

    public String getIdcardBack() {
        return idcardBack;
    }

    public void setIdcardBack(String idcardBack) {
        this.idcardBack = idcardBack;
    }

    public String getIdcardPic() {
        return idcardPic;
    }

    public void setIdcardPic(String idcardPic) {
        this.idcardPic = idcardPic;
    }

    public Byte getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(Byte certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public Integer getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(Integer auditUser) {
        this.auditUser = auditUser;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        KgUserCertificated other = (KgUserCertificated) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getRealName() == null ? other.getRealName() == null : this.getRealName().equals(other.getRealName()))
            && (this.getIdcardNo() == null ? other.getIdcardNo() == null : this.getIdcardNo().equals(other.getIdcardNo()))
            && (this.getIdcardFront() == null ? other.getIdcardFront() == null : this.getIdcardFront().equals(other.getIdcardFront()))
            && (this.getIdcardBack() == null ? other.getIdcardBack() == null : this.getIdcardBack().equals(other.getIdcardBack()))
            && (this.getIdcardPic() == null ? other.getIdcardPic() == null : this.getIdcardPic().equals(other.getIdcardPic()))
            && (this.getCertificateStatus() == null ? other.getCertificateStatus() == null : this.getCertificateStatus().equals(other.getCertificateStatus()))
            && (this.getRefuseReason() == null ? other.getRefuseReason() == null : this.getRefuseReason().equals(other.getRefuseReason()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getAuditDate() == null ? other.getAuditDate() == null : this.getAuditDate().equals(other.getAuditDate()))
            && (this.getAuditUser() == null ? other.getAuditUser() == null : this.getAuditUser().equals(other.getAuditUser()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getRealName() == null) ? 0 : getRealName().hashCode());
        result = prime * result + ((getIdcardNo() == null) ? 0 : getIdcardNo().hashCode());
        result = prime * result + ((getIdcardFront() == null) ? 0 : getIdcardFront().hashCode());
        result = prime * result + ((getIdcardBack() == null) ? 0 : getIdcardBack().hashCode());
        result = prime * result + ((getIdcardPic() == null) ? 0 : getIdcardPic().hashCode());
        result = prime * result + ((getCertificateStatus() == null) ? 0 : getCertificateStatus().hashCode());
        result = prime * result + ((getRefuseReason() == null) ? 0 : getRefuseReason().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getAuditDate() == null) ? 0 : getAuditDate().hashCode());
        result = prime * result + ((getAuditUser() == null) ? 0 : getAuditUser().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", realName=").append(realName);
        sb.append(", idcardNo=").append(idcardNo);
        sb.append(", idcardFront=").append(idcardFront);
        sb.append(", idcardBack=").append(idcardBack);
        sb.append(", idcardPic=").append(idcardPic);
        sb.append(", certificateStatus=").append(certificateStatus);
        sb.append(", refuseReason=").append(refuseReason);
        sb.append(", createDate=").append(createDate);
        sb.append(", auditDate=").append(auditDate);
        sb.append(", auditUser=").append(auditUser);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}