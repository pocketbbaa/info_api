package com.kg.platform.dao.entity;

import java.util.Date;

public class UserLog {
    private Long logId;

    private Long userId;

    private String userEmail;

    private String userPhone;

    private String userIp;

    private Date logTime;

    private Boolean userSource;

    private Byte logType;

    private String logDetail;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public Boolean getUserSource() {
        return userSource;
    }

    public void setUserSource(Boolean userSource) {
        this.userSource = userSource;
    }

    public Byte getLogType() {
        return logType;
    }

    public void setLogType(Byte logType) {
        this.logType = logType;
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail;
    }
}