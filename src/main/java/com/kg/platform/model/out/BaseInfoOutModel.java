package com.kg.platform.model.out;

import java.util.Date;

public class BaseInfoOutModel {
    private Integer infoId;

    private String infoType;

    private String infoDetail;

    private Boolean infoStatus;

    private Integer infoOrder;

    private Date createDate;

    private Integer createUser;

    private Date updateDate;

    private Integer updateUser;

    private String infoName;

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getInfoDetail() {
        return infoDetail;
    }

    public void setInfoDetail(String infoDetail) {
        this.infoDetail = infoDetail;
    }

    public Boolean getInfoStatus() {
        return infoStatus;
    }

    public void setInfoStatus(Boolean infoStatus) {
        this.infoStatus = infoStatus;
    }

    public Integer getInfoOrder() {
        return infoOrder;
    }

    public void setInfoOrder(Integer infoOrder) {
        this.infoOrder = infoOrder;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }
}