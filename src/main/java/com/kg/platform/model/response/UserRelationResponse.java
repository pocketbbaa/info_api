package com.kg.platform.model.response;

import java.io.Serializable;

public class UserRelationResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1078851717595842122L;

    private Long inviteCount;

    private String inviteCode;

    private Integer inviteStatus;

    private String inviteFreezeReason;

    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(Long inviteCount) {
        this.inviteCount = inviteCount;
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

    public String getInviteFreezeReason() {
        return inviteFreezeReason;
    }

    public void setInviteFreezeReason(String inviteFreezeReason) {
        this.inviteFreezeReason = inviteFreezeReason;
    }

}
