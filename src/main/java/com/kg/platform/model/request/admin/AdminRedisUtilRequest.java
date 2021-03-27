package com.kg.platform.model.request.admin;

/**
 * Created by Administrator on 2018/8/3.
 */
public class AdminRedisUtilRequest {
    private String userId;
    private String access;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}
