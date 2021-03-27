package com.kg.platform.model.out;

import java.util.Date;

/**
 * 
 * @author Asus 123
 *
 */
public class UserConcernListOutModel {

    private String userId;

    private String userName;

    private String avatar;

    private Integer concernedStatus;

    private Integer concernUserStatus;

    private String columnName;

    private String resume;

    private int vipTag;

    private int articleNum;

    private int userRole;

    private int realnameAuthed;

    private int columnAuthed;

    private Date createDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getVipTag() {
        return vipTag;
    }

    public void setVipTag(int vipTag) {
        this.vipTag = vipTag;
    }

    public int getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(int articleNum) {
        this.articleNum = articleNum;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public int getRealnameAuthed() {
        return realnameAuthed;
    }

    public void setRealnameAuthed(int realnameAuthed) {
        this.realnameAuthed = realnameAuthed;
    }

    public int getColumnAuthed() {
        return columnAuthed;
    }

    public void setColumnAuthed(int columnAuthed) {
        this.columnAuthed = columnAuthed;
    }

    public Integer getConcernedStatus() {
        return concernedStatus;
    }

    public void setConcernedStatus(Integer concernedStatus) {
        this.concernedStatus = concernedStatus;
    }

    public Integer getConcernUserStatus() {
        return concernUserStatus;
    }

    public void setConcernUserStatus(Integer concernUserStatus) {
        this.concernUserStatus = concernUserStatus;
    }
}