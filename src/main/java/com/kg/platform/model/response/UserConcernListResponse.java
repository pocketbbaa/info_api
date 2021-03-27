package com.kg.platform.model.response;

import java.io.Serializable;

public class UserConcernListResponse implements Serializable{

    private String userId;

    private String userName;

    private String avatar;

    private Integer concernedStatus;

    private Integer concernUserStatus;

    private String columnName;

    private Integer vipTag;

    private Integer articleNum;

    private Integer userRole;

    private String resume;

    private int realAuthedTag;

    private Integer publishKind;

	public Integer getPublishKind() {
		return publishKind;
	}

	public void setPublishKind(Integer publishKind) {
		this.publishKind = publishKind;
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

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getVipTag() {
        return vipTag;
    }

    public void setVipTag(Integer vipTag) {
        this.vipTag = vipTag;
    }

    public Integer getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(Integer articleNum) {
        this.articleNum = articleNum;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public int getRealAuthedTag() {
        return realAuthedTag;
    }

    public void setRealAuthedTag(int realAuthedTag) {
        this.realAuthedTag = realAuthedTag;
    }
}