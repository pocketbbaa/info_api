package com.kg.platform.model.in;

import com.kg.platform.model.request.BaseRequest;

import java.util.List;

public class UserRelationInModel extends BaseRequest {
    /**
     * 
     */
    private static final long serialVersionUID = -6887224681370650047L;

    private Long userId;

    private Integer activityId;

    private Integer inviteCount;

    private Integer auditStatus;

    private String startTime;

    private String endTime;



    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(Integer inviteCount) {
        this.inviteCount = inviteCount;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

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


}