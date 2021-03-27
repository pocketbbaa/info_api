package com.kg.platform.model.in;

import java.util.Date;

import com.kg.platform.model.request.BaseRequest;

public class ActivityInviteLogInModel extends BaseRequest {
    /**
     * 
     */
    private static final long serialVersionUID = -6887224681370650047L;

    private Long inviteId;;

    private Long userId;

    private Long relUserId;

    private Integer activityId;

    private Integer status;

    private Date createTime;

    public Long getInviteId() {
        return inviteId;
    }

    public void setInviteId(Long inviteId) {
        this.inviteId = inviteId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRelUserId() {
        return relUserId;
    }

    public void setRelUserId(Long relUserId) {
        this.relUserId = relUserId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}