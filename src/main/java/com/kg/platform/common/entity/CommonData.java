package com.kg.platform.common.entity;

/**
 * Created by Administrator on 2018/7/20.
 */
public class CommonData{
    private String userId;//用户ID
    private String deviceId;//设备ID
    private String deviceType;//设备类型

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

}
