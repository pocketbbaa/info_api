package com.kg.platform.model.request;

import java.io.Serializable;

/**
 * @author 
 */
public class KgNewsFlashRequest implements Serializable {
    /**
     * 快讯ID
     */
    private Long newsflashId;

    /**
     * 快讯标题
     */
    private String newsflashTitle;

    /**
     * 快讯原文链接
     */
    private String newsflashLink;

    /**
     * 快讯类型 0：区块链快讯 1：金融快讯 2：股市快讯
     */
    private Integer newsflashType;

    /**
     * 预留字段
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 快讯正文
     */
    private String newsflashText;

    private int currentPage;

    private int pageSize;

    private Integer newsflashOrigin;//0：系统抓取 1：人工添加

    private Long createUser;//发布人

    private Integer ifPush;//是否已推送 0：未推送 1：已推送

    private Integer level;//重要级别 0：不重要 1：重要

    private String newsflashBottomImg;//快讯分享底图

    private Integer displayStatus;//显示状态 0：隐藏 1：显示

    private String createUserName;//发布人名称

    private Long updateUser;//修改人ID

    private String updateUserName;//修改人名称

	private String userIp;

    private static final long serialVersionUID = 1L;

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Integer getNewsflashOrigin() {
        return newsflashOrigin;
    }

    public void setNewsflashOrigin(Integer newsflashOrigin) {
        this.newsflashOrigin = newsflashOrigin;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Integer getIfPush() {
        return ifPush;
    }

    public void setIfPush(Integer ifPush) {
        this.ifPush = ifPush;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getNewsflashBottomImg() {
        return newsflashBottomImg;
    }

    public void setNewsflashBottomImg(String newsflashBottomImg) {
        this.newsflashBottomImg = newsflashBottomImg;
    }

    public Integer getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(Integer displayStatus) {
        this.displayStatus = displayStatus;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getNewsflashId() {
        return newsflashId;
    }

    public void setNewsflashId(Long newsflashId) {
        this.newsflashId = newsflashId;
    }

    public String getNewsflashTitle() {
        return newsflashTitle;
    }

    public void setNewsflashTitle(String newsflashTitle) {
        this.newsflashTitle = newsflashTitle;
    }

    public String getNewsflashLink() {
        return newsflashLink;
    }

    public void setNewsflashLink(String newsflashLink) {
        this.newsflashLink = newsflashLink;
    }

    public Integer getNewsflashType() {
        return newsflashType;
    }

    public void setNewsflashType(Integer newsflashType) {
        this.newsflashType = newsflashType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getNewsflashText() {
        return newsflashText;
    }

    public void setNewsflashText(String newsflashText) {
        this.newsflashText = newsflashText;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        KgNewsFlashRequest other = (KgNewsFlashRequest) that;
        return (this.getNewsflashId() == null ? other.getNewsflashId() == null : this.getNewsflashId().equals(other.getNewsflashId()))
            && (this.getNewsflashTitle() == null ? other.getNewsflashTitle() == null : this.getNewsflashTitle().equals(other.getNewsflashTitle()))
            && (this.getNewsflashLink() == null ? other.getNewsflashLink() == null : this.getNewsflashLink().equals(other.getNewsflashLink()))
            && (this.getNewsflashType() == null ? other.getNewsflashType() == null : this.getNewsflashType().equals(other.getNewsflashType()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getNewsflashText() == null ? other.getNewsflashText() == null : this.getNewsflashText().equals(other.getNewsflashText()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getNewsflashId() == null) ? 0 : getNewsflashId().hashCode());
        result = prime * result + ((getNewsflashTitle() == null) ? 0 : getNewsflashTitle().hashCode());
        result = prime * result + ((getNewsflashLink() == null) ? 0 : getNewsflashLink().hashCode());
        result = prime * result + ((getNewsflashType() == null) ? 0 : getNewsflashType().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getNewsflashText() == null) ? 0 : getNewsflashText().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", newsflashId=").append(newsflashId);
        sb.append(", newsflashTitle=").append(newsflashTitle);
        sb.append(", newsflashLink=").append(newsflashLink);
        sb.append(", newsflashType=").append(newsflashType);
        sb.append(", remark=").append(remark);
        sb.append(", createDate=").append(createDate);
        sb.append(", newsflashText=").append(newsflashText);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}