package com.kg.platform.model.in;

import java.util.Date;

public class FriendlyLinkInModel {
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

    private Date updateDate;

    private Integer start;

    private Integer limit;

	/**
	 * 1:合作伙伴 2：友链
	 */
	private Integer type;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
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