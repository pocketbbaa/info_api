package com.kg.platform.dao.entity;

import java.util.Date;

public class SysPostAuth {
    private Integer postAuthId;

    private Integer authId;

    private Integer postId;

    private Integer createUser;

    private Date createDate;

    public Integer getPostAuthId() {
        return postAuthId;
    }

    public void setPostAuthId(Integer postAuthId) {
        this.postAuthId = postAuthId;
    }

    public Integer getAuthId() {
        return authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
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
}