package com.kg.platform.model.response;


public class FriendlyLinkResponse {
    private Integer linkId;

    private String linkName;

    private String linkAddress;

    private Integer linkStatus;

    private Integer linkOrder;

    private String linkIcon;

    private String updateUser;

    private String createDate;

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
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

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
}