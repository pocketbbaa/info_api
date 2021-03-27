package com.kg.platform.model.in;

import java.util.Date;

public class HotsearchInModel {
    private Integer searchwordId;

    private String searchwordDesc;

    private Boolean searchwordStatus;

    private Integer searchwordOrder;

    private Integer createUser;

    private Date createDate;

    private Integer updateUser;

    private Date updateDate;

    public Integer getSearchwordId() {
        return searchwordId;
    }

    public void setSearchwordId(Integer searchwordId) {
        this.searchwordId = searchwordId;
    }

    public String getSearchwordDesc() {
        return searchwordDesc;
    }

    public void setSearchwordDesc(String searchwordDesc) {
        this.searchwordDesc = searchwordDesc;
    }

    public Boolean getSearchwordStatus() {
        return searchwordStatus;
    }

    public void setSearchwordStatus(Boolean searchwordStatus) {
        this.searchwordStatus = searchwordStatus;
    }

    public Integer getSearchwordOrder() {
        return searchwordOrder;
    }

    public void setSearchwordOrder(Integer searchwordOrder) {
        this.searchwordOrder = searchwordOrder;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}