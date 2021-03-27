package com.kg.platform.model.request.admin;

public class AdminUserLockRequest {

    private String userId;

    private Long lockUserId;

    private Integer lockTime;

    private Integer lockUnit;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getLockUserId() {
        return lockUserId;
    }

    public void setLockUserId(Long lockUserId) {
        this.lockUserId = lockUserId;
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
}
