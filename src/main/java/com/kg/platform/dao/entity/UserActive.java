package com.kg.platform.dao.entity;

import java.util.Date;

public class UserActive {
    private Long userId;

    private String lastloginIp;

    private Date lastloginTime;

    private Date updateDate;

    private Long updateUserid;

    private Date lockDate;

    private Integer lockTime;

    private Integer lockUnit;

    private Integer bowseNum;

    private Integer shareNum;

    private Date applyColumnTime;

    private Integer auditUserid;

    private Date auditDate;

    private String refuseReason;

    private Integer inviteStatus;

    private Integer bonusStatus;

    private String bonusFreezeReason;

    private Integer checkUser;

    private Date checkDate;

    private Integer freezeUser;

    private Date freezeDate;

    public Integer getFreezeUser() {
        return freezeUser;
    }

    public void setFreezeUser(Integer freezeUser) {
        this.freezeUser = freezeUser;
    }

    public Date getFreezeDate() {
        return freezeDate;
    }

    public void setFreezeDate(Date freezeDate) {
        this.freezeDate = freezeDate;
    }

    public Integer getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(Integer checkUser) {
        this.checkUser = checkUser;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
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

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLastloginIp() {
        return lastloginIp;
    }

    public void setLastloginIp(String lastloginIp) {
        this.lastloginIp = lastloginIp;
    }

    public Date getLastloginTime() {
        return lastloginTime;
    }

    public void setLastloginTime(Date lastloginTime) {
        this.lastloginTime = lastloginTime;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUpdateUserid() {
        return updateUserid;
    }

    public void setUpdateUserid(Long updateUserid) {
        this.updateUserid = updateUserid;
    }

    public Date getLockDate() {
        return lockDate;
    }

    public void setLockDate(Date lockDate) {
        this.lockDate = lockDate;
    }

    public Integer getLockTime() {
        return lockTime;
    }

    public void setLockTime(Integer lockTime) {
        this.lockTime = lockTime;
    }

    public Integer getLockUnit() {
        return lockUnit;
    }

    public void setLockUnit(Integer lockUnit) {
        this.lockUnit = lockUnit;
    }

    public Integer getBowseNum() {
        return bowseNum;
    }

    public void setBowseNum(Integer bowseNum) {
        this.bowseNum = bowseNum;
    }

    public Integer getShareNum() {
        return shareNum;
    }

    public void setShareNum(Integer shareNum) {
        this.shareNum = shareNum;
    }

    public Date getApplyColumnTime() {
        return applyColumnTime;
    }

    public void setApplyColumnTime(Date applyColumnTime) {
        this.applyColumnTime = applyColumnTime;
    }

    public Integer getAuditUserid() {
        return auditUserid;
    }

    public void setAuditUserid(Integer auditUserid) {
        this.auditUserid = auditUserid;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

}