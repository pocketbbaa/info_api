package com.kg.platform.dao.entity;

import java.util.Date;

public class UserRelation {
    private Long relId;

    private Long userId;

    private Long relUserId;

    private Integer bonusStatus;

    private Integer activityId;

    private Integer relType;

    private Date relTime;

    private String subName;

    private String userName;

    // 更新人数
    private Long targrtCount;

    public Long getRelId() {
        return relId;
    }

    public void setRelId(Long relId) {
        this.relId = relId;
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

    public Integer getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(Integer bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    public Integer getRelType() {
        return relType;
    }

    public void setRelType(Integer relType) {
        this.relType = relType;
    }

    public Date getRelTime() {
        return relTime;
    }

    public void setRelTime(Date relTime) {
        this.relTime = relTime;
    }

    public Long getTargrtCount() {
        return targrtCount;
    }

    public void setTargrtCount(Long targrtCount) {
        this.targrtCount = targrtCount;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}