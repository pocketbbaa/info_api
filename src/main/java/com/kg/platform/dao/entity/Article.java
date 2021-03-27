package com.kg.platform.dao.entity;

import java.util.Date;

public class Article {
    private String articleId;

    private String articleTitle;

    private String articleTags;

    private String articleDescription;

    private String articleImage;

    private Integer articleType;

    private String articleSource;

    private String articleLink;

    private Integer columnId;

    private Integer secondColumn;

    private Integer displayStatus;

    private Integer displayOrder;

    private Boolean commentSet;

    private Integer publishSet;

    private Date publishTime;

    private Integer publishStatus;

    private String createUser;

    private Date createDate;

    private Date updateDate;

    private String updateUser;

    private Integer sysUser;

    private Integer updateSysUser;

    private String refuseReason;

    private Integer operBonusUser;

    private String articleText;

    private Long auditUser;

    private Date auditDate;

    private Integer bowseNum;

    private String tagnames;

    private Integer bonusStatus;

    private String username;

    private Integer textnum;

    private Integer browseNum;

    private Integer shareNum;

    private Integer thumbupNum;

    private Integer collectNum;

    private Integer articleFrom;

    private Integer publishKind; // 发布类型 1 文章 2 视频

    private String videoUrl;

    private String videoFilename;

    private String addedBonusReason;

    private String freezeReason;

    private Integer articleMark;

    private Integer publishBonusStatus;

    private Integer operFreezeUser;

    private Date operFreezeTime;

    private Date operBonusTime;

    private Date addedBonusTime;

    private Integer ifPlatformPublishAward;//是否设有奖励 0：否 1：是

    private Integer ifPush;//是否推送 0：否 1：是

    private Integer articleImgSize;//1：小图  2：大图

    private Integer ifIntoIndex;//是否展示到首页推荐列表 0：否 1：是

    private Integer titleModify; //是否可以修改标题0：否，1：是

	private Date publishDate;//发布时间

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public Integer getTitleModify() {
        return titleModify;
    }

    public void setTitleModify(Integer titleModify) {
        this.titleModify = titleModify;
    }

    public Integer getIfIntoIndex() {
        return ifIntoIndex;
    }

    public void setIfIntoIndex(Integer ifIntoIndex) {
        this.ifIntoIndex = ifIntoIndex;
    }

    public Integer getArticleImgSize() {
        return articleImgSize;
    }

    public void setArticleImgSize(Integer articleImgSize) {
        this.articleImgSize = articleImgSize;
    }

    public Integer getIfPlatformPublishAward() {
        return ifPlatformPublishAward;
    }

    public void setIfPlatformPublishAward(Integer ifPlatformPublishAward) {
        this.ifPlatformPublishAward = ifPlatformPublishAward;
    }

    public Integer getIfPush() {
        return ifPush;
    }

    public void setIfPush(Integer ifPush) {
        this.ifPush = ifPush;
    }

    public String getVideoFilename() {
        return videoFilename;
    }

    public void setVideoFilename(String videoFilename) {
        this.videoFilename = videoFilename;
    }

    public Integer getPublishKind() {
        return publishKind;
    }

    public void setPublishKind(Integer publishKind) {
        this.publishKind = publishKind;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getArticleFrom() {
        return articleFrom;
    }

    public void setArticleFrom(Integer articleFrom) {
        this.articleFrom = articleFrom;
    }

    public Integer getTextnum() {
        return textnum;
    }

    public void setTextnum(Integer textnum) {
        this.textnum = textnum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(Integer bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    public String getTagnames() {
        return tagnames;
    }

    public void setTagnames(String tagnames) {
        this.tagnames = tagnames;
    }

    public Long getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(Long auditUser) {
        this.auditUser = auditUser;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public Integer getBowseNum() {
        return bowseNum;
    }

    public void setBowseNum(Integer bowseNum) {
        this.bowseNum = bowseNum;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleTags() {
        return articleTags;
    }

    public void setArticleTags(String articleTags) {
        this.articleTags = articleTags;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }

    public Integer getArticleType() {
        return articleType;
    }

    public void setArticleType(Integer articleType) {
        this.articleType = articleType;
    }

    public String getArticleSource() {
        return articleSource;
    }

    public void setArticleSource(String articleSource) {
        this.articleSource = articleSource;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public Integer getSecondColumn() {
        return secondColumn;
    }

    public void setSecondColumn(Integer secondColumn) {
        this.secondColumn = secondColumn;
    }

    public Integer getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(Integer displayStatus) {
        this.displayStatus = displayStatus;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getCommentSet() {
        return commentSet;
    }

    public void setCommentSet(Boolean commentSet) {
        this.commentSet = commentSet;
    }

    public Integer getPublishSet() {
        return publishSet;
    }

    public void setPublishSet(Integer publishSet) {
        this.publishSet = publishSet;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getUpdateSysUser() {
        return updateSysUser;
    }

    public void setUpdateSysUser(Integer updateSysUser) {
        this.updateSysUser = updateSysUser;
    }

    public Integer getSysUser() {
        return sysUser;
    }

    public void setSysUser(Integer sysUser) {
        this.sysUser = sysUser;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    public Integer getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(Integer browseNum) {
        this.browseNum = browseNum;
    }

    public Integer getShareNum() {
        return shareNum;
    }

    public void setShareNum(Integer shareNum) {
        this.shareNum = shareNum;
    }

    public Integer getThumbupNum() {
        return thumbupNum;
    }

    public void setThumbupNum(Integer thumbupNum) {
        this.thumbupNum = thumbupNum;
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public Integer getArticleMark() {
        return articleMark;
    }

    public void setArticleMark(Integer articleMark) {
        this.articleMark = articleMark;
    }

    public String getAddedBonusReason() {
        return addedBonusReason;
    }

    public void setAddedBonusReason(String addedBonusReason) {
        this.addedBonusReason = addedBonusReason;
    }

    public String getFreezeReason() {
        return freezeReason;
    }

    public void setFreezeReason(String freezeReason) {
        this.freezeReason = freezeReason;
    }

    public Integer getOperBonusUser() {
        return operBonusUser;
    }

    public void setOperBonusUser(Integer operBonusUser) {
        this.operBonusUser = operBonusUser;
    }

    public Integer getPublishBonusStatus() {
        return publishBonusStatus;
    }

    public void setPublishBonusStatus(Integer publishBonusStatus) {
        this.publishBonusStatus = publishBonusStatus;
    }

    public Integer getOperFreezeUser() {
        return operFreezeUser;
    }

    public void setOperFreezeUser(Integer operFreezeUser) {
        this.operFreezeUser = operFreezeUser;
    }

    public Date getOperFreezeTime() {
        return operFreezeTime;
    }

    public void setOperFreezeTime(Date operFreezeTime) {
        this.operFreezeTime = operFreezeTime;
    }

    public Date getOperBonusTime() {
        return operBonusTime;
    }

    public void setOperBonusTime(Date operBonusTime) {
        this.operBonusTime = operBonusTime;
    }

    public Date getAddedBonusTime() {
        return addedBonusTime;
    }

    public void setAddedBonusTime(Date addedBonusTime) {
        this.addedBonusTime = addedBonusTime;
    }

}
