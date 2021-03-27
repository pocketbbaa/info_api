package com.kg.platform.dao.entity;

import java.util.Date;

public class UserConcern {

    private  Long  concernId;

    private  Long  userId;

    private  Long  concernUserId;

    private Integer  concernStatus;

    private Date createDate;

    public Long getConcernId() {
        return concernId;
    }

    public void setConcernId(Long concernId) {
        this.concernId = concernId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getConcernUserId() {
        return concernUserId;
    }

    public void setConcernUserId(Long concernUserId) {
        this.concernUserId = concernUserId;
    }

    public Integer getConcernStatus() {
        return concernStatus;
    }

    public void setConcernStatus(Integer concernStatus) {
        this.concernStatus = concernStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}