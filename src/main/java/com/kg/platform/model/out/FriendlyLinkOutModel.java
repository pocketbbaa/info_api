package com.kg.platform.model.out;

import java.util.Date;

public class FriendlyLinkOutModel {
    private Integer linkId;

    private String linkName;

    private String linkAddress;

    private Integer secondChannel;

    private Integer linkStatus;

    private Integer linkOrder;

    private String linkIcon;

    private Integer createUser;

    private Date createDate;

    private Integer updateUser;

    private String createUserName;

    private Date updateDate;

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public Integer getSecondChannel() {
        return secondChannel;
    }

    public void setSecondChannel(Integer secondChannel) {
        this.secondChannel = secondChannel;
    }

	public Integer getLinkStatus() {
		return linkStatus;
	}

	public void setLinkStatus(Integer linkStatus) {
		this.linkStatus = linkStatus;
	}

	public Integer getLinkOrder() {
        return linkOrder;
    }

    public void setLinkOrder(Integer linkOrder) {
        this.linkOrder = linkOrder;
    }

    public String getLinkIcon() {
        return linkIcon;
    }

    public void setLinkIcon(String linkIcon) {
        this.linkIcon = linkIcon;
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