package com.kg.platform.model.request;

import java.util.Date;
import java.util.List;

public class FriendlyLinkRequest {
    private Integer linkId;

    private List<Integer> linkIdList;

    private String linkName;

    private String linkAddress;

    private Integer secondChannel;

    private Integer linkStatus;

    private Integer linkOrder;

    private String linkIcon;

    private Integer createUser;

    private String createDate;

    private Integer updateUser;

    private String updateDate;

	/**
	 * 1:合作伙伴 2：友链
	 */
	private Integer type;

	private Integer currentPage;

	private Integer pageSize;

	public List<Integer> getLinkIdList() {
		return linkIdList;
	}

	public void setLinkIdList(List<Integer> linkIdList) {
		this.linkIdList = linkIdList;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
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

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

	public Integer getLinkStatus() {
		return linkStatus;
	}

	public void setLinkStatus(Integer linkStatus) {
		this.linkStatus = linkStatus;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
}