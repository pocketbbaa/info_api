package com.kg.platform.model.response.admin;

import java.util.Date;

/**
 * 文件列表出参： 文章ID、标题、所属栏目、发布人、发布时间、更新人、更新时间、状态、审核人、审核时间、显示状态、访问量、排序
 *
 */
public class ArticleQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = 4008056123198645062L;

    private String articleId;

    private String articleTitle;

    private String columnName;

    private String secondColumnName;

    private String createUser;

    private String createDate;

    private String updateUser;

    private String updateDate;

    private Integer publishStatus;

    private String publishStatusDisplay;

    private String auditUser;

    private String auditDate;

    private Integer displayStatus;

    private String displayStatusDisplay;

    private Integer bowseNum;

    private Integer displayOrder;

    private String sysUser;

    private String updateSysUser;

    private Date publishTime;

    private Integer bonusStatus;

    private String username;

    private Integer textnum;

    private Integer publishKind; // 发布类型 1 文章 2 视频

    private String videoUrl;

    private String videoFilename;

    private Integer columnId;

    private Integer secondColumn;

    private Integer articleType;

    private Integer articleMark;

    private String articleDescription;

    private String articleImage;

    private Integer ifPlatformPublishAward;//是否设有奖励 0：否 1：是

    private Integer ifPush;//是否推送 0：否 1：是

    private Integer articleImgSize;//1：小图  2：大图

    private Integer ifIntoIndex;

    private String blockchainUrl; //文章上链后的查看地址

	private String publishDate;//发布时间

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getBlockchainUrl() {
        return blockchainUrl;
    }

    public void setBlockchainUrl(String blockchainUrl) {
        this.blockchainUrl = blockchainUrl;
    }

    public Integer getSecondColumn() {
        return secondColumn;
    }

    public void setSecondColumn(Integer secondColumn) {
        this.secondColumn = secondColumn;
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

    public String getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public Integer getArticleMark() {
        return articleMark;
    }

    public void setArticleMark(Integer articleMark) {
        this.articleMark = articleMark;
    }

    public Integer getArticleType() {
        return articleType;
    }

    public void setArticleType(Integer articleType) {
        this.articleType = articleType;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
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

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getSysUser() {
        return sysUser;
    }

    public void setSysUser(String sysUser) {
        this.sysUser = sysUser;
    }

    public String getUpdateSysUser() {
        return updateSysUser;
    }

    public void setUpdateSysUser(String updateSysUser) {
        this.updateSysUser = updateSysUser;
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

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getSecondColumnName() {
        return secondColumnName;
    }

    public void setSecondColumnName(String secondColumnName) {
        this.secondColumnName = secondColumnName;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getPublishStatusDisplay() {
        return publishStatusDisplay;
    }

    public void setPublishStatusDisplay(String publishStatusDisplay) {
        this.publishStatusDisplay = publishStatusDisplay;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public Integer getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(Integer displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatusDisplay() {
        return displayStatusDisplay;
    }

    public void setDisplayStatusDisplay(String displayStatusDisplay) {
        this.displayStatusDisplay = displayStatusDisplay;
    }

    public Integer getBowseNum() {
        return bowseNum;
    }

    public void setBowseNum(Integer bowseNum) {
        this.bowseNum = bowseNum;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
