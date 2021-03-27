package com.kg.platform.dao.entity;

import java.util.Date;

public class SysUser {
    private Long sysUserId;

    private Integer postId;

    private String sysUserName;

    private String sysUserPassword;

    private String userMobile;

    private String userRealname;

    private Date createDate;

    private Integer createUser;

    private Date updateDate;

    private Integer updateUser;

    private Boolean status;

    private String kgUserId;

    private String kgUserName;

    public String getKgUserId() {
        return kgUserId;
    }

    public void setKgUserId(String kgUserId) {
        this.kgUserId = kgUserId;
    }

    public String getKgUserName() {
        return kgUserName;
    }

    public void setKgUserName(String kgUserName) {
        this.kgUserName = kgUserName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public String getSysUserPassword() {
        return sysUserPassword;
    }

    public void setSysUserPassword(String sysUserPassword) {
        this.sysUserPassword = sysUserPassword;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserRealname() {
        return userRealname;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname;
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