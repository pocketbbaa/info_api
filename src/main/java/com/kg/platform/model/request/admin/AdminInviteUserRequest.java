package com.kg.platform.model.request.admin;

public class AdminInviteUserRequest {

    private String userId;

    private Integer auditUserId;

    private Integer bonusStatus;

    private String bonusFreezeReason;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(Integer auditUserId) {
        this.auditUserId = auditUserId;
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

}
