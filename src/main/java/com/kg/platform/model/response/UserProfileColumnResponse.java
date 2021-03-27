package com.kg.platform.model.response;

import java.io.Serializable;

public class UserProfileColumnResponse implements Serializable {

    private static final long serialVersionUID = 8937025962837992127L;

    private String createUser;  //用户ID

    private long artsum;// 文章数

    private int pbowsenum;  //浏览量

    private int comments;  //评论数

    private String columnName;  //作者名

    private String columnIntro;  //专栏介绍

    private String columnAvatar;  //专栏头像

    private long thumbupNum;// 作者点赞数

    private Integer userRole; // 用户角色

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public long getArtsum() {
        return artsum;
    }

    public void setArtsum(long artsum) {
        this.artsum = artsum;
    }

    public int getPbowsenum() {
        return pbowsenum;
    }

    public void setPbowsenum(int pbowsenum) {
        this.pbowsenum = pbowsenum;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnIntro() {
        return columnIntro;
    }

    public void setColumnIntro(String columnIntro) {
        this.columnIntro = columnIntro;
    }

    public String getColumnAvatar() {
        return columnAvatar;
    }

    public void setColumnAvatar(String columnAvatar) {
        this.columnAvatar = columnAvatar;
    }

    public long getThumbupNum() {
        return thumbupNum;
    }

    public void setThumbupNum(long thumbupNum) {
        this.thumbupNum = thumbupNum;
    }
}