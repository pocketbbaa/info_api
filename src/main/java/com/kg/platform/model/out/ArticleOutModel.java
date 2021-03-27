package com.kg.platform.model.out;

import java.util.Date;

import com.kg.platform.model.request.BaseRequest;

public class ArticleOutModel extends BaseRequest {
    /**
     * 
     */
    private static final long serialVersionUID = 7771775673911603065L;

    private String columnname;// 栏目名称

    private String articleTagnames;

    private String profilename;// 专栏名称

    private String profileavatar;// 专栏头像

    private int collect;// 收藏数

    private long collectstatus;// 收藏状态

    private int comments;// 评论数

    private long articlefrom;// 来源

    private String username;

    private String articleId;

    private int isPublishBonus;

    private int articleMark;

    private int publishBonusStatus;

    private String freezeReason;

    private String articleTitle;

    private String articleTags;

    private String articleDescription;

    private String articleImage;

    private Integer articleType;

    private String articleSource;

    private String articleLink;

    private Integer columnId;

    private Integer secondColumn;

    private long displayStatus;

    private Integer displayOrder;

    private long commentSet;

    private Boolean publishSet;

    private Date publishTime;

    private Integer publishStatus;

    private Long createUser;

    private Date createDate;

    private Date updateDate;

    private Long updateUser;

    private Integer auditUser;

    private Date auditDate;

    private Integer sysUser;

    private String refuseReason;

    private Integer bowseNum;

    private Integer browseNum;

    private String articleText;

    private String tagnames;

    private String secondcolumnname;

    private String userColumn;

    private String columnAvatar;

    private long thumbupNum;

    private int bonusStatus;// 是否开启打赏

    private long bonus;// 是否有阅读奖励

    private long praisestatus;// 用户是否点赞过 0：未点赞

    private int getStatus;// 领取浏览奖励状态

    private Long textnum;

    private String searchword;

    private Integer publishKind; // 发布类型 1 文章 2 视频

    private String videoUrl;

    private String videoFilename;

    private Integer ifPlatformPublishAward;//是否设有奖励 0：否 1：是

    private Integer ifPush;//是否推送 0：否 1：是

    private Integer articleImgSize;//1：小图  2：大图

    private int collectStatusInfo; // 0未点赞和收藏   1 已收藏  2 已点赞  3已点赞和收藏

    private String blockchainUrl; //文章上链后的查看地址

	private Date publishDate;//发布时间

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getBlockchainUrl() {
        return blockchainUrl;
    }

    public void setBlockchainUrl(String blockchainUrl) {
        this.blockchainUrl = blockchainUrl;
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

    public String getSearchword() {
        return searchword;
    }

    public void setSearchword(String searchword) {
        this.searchword = searchword;
    }

    public Long getTextnum() {
        return textnum;
    }

    public void setTextnum(Long textnum) {
        this.textnum = textnum;
    }

    /**
     * @return the getStatus
     */
    public int getGetStatus() {
        return getStatus;
    }

    /**
     * @param getStatus
     *            the getStatus to set
     */
    public void setGetStatus(int getStatus) {
        this.getStatus = getStatus;
    }

    /**
     * @return the praisestatus
     */
    public long getPraisestatus() {
        return praisestatus;
    }

    /**
     * @param praisestatus
     *            the praisestatus to set
     */
    public void setPraisestatus(long praisestatus) {
        this.praisestatus = praisestatus;
    }

    /**
     * @return the articlefrom
     */
    public long getArticlefrom() {
        return articlefrom;
    }

    /**
     * @param articlefrom
     *            the articlefrom to set
     */
    public void setArticlefrom(long articlefrom) {
        this.articlefrom = articlefrom;
    }

    /**
     * @return the bonus
     */
    public long getBonus() {
        return bonus;
    }

    /**
     * @param bonus
     *            the bonus to set
     */
    public void setBonus(long bonus) {
        this.bonus = bonus;
    }

    /**
     * @return the bonusStatus
     */
    public int getBonusStatus() {
        return bonusStatus;
    }

    /**
     * @param bonusStatus
     *            the bonusStatus to set
     */
    public void setBonusStatus(int bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    /**
     * @return the thumbupNum
     */
    public long getThumbupNum() {
        return thumbupNum;
    }

    /**
     * @param thumbupNum
     *            the thumbupNum to set
     */
    public void setThumbupNum(long thumbupNum) {
        this.thumbupNum = thumbupNum;
    }

    /**
     * @return the secondcolumnname
     */
    public String getSecondcolumnname() {
        return secondcolumnname;
    }

    /**
     * @param secondcolumnname
     *            the secondcolumnname to set
     */
    public void setSecondcolumnname(String secondcolumnname) {
        this.secondcolumnname = secondcolumnname;
    }

    /**
     * @return the collectstatus
     */
    public long getCollectstatus() {
        return collectstatus;
    }

    /**
     * @param collectstatus
     *            the collectstatus to set
     */
    public void setCollectstatus(long collectstatus) {
        this.collectstatus = collectstatus;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the columnname
     */
    public String getColumnname() {
        return columnname;
    }

    /**
     * @param columnname
     *            the columnname to set
     */
    public void setColumnname(String columnname) {
        this.columnname = columnname;
    }

    public String getTagnames() {
        return tagnames;
    }

    public void setTagnames(String tagnames) {
        this.tagnames = tagnames;
    }

    /**
     * @return the articleId
     */
    public String getArticleId() {
        return articleId;
    }

    /**
     * @param articleId
     *            the articleId to set
     */
    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    /**
     * @return the articleTitle
     */
    public String getArticleTitle() {
        return articleTitle;
    }

    /**
     * @param articleTitle
     *            the articleTitle to set
     */
    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    /**
     * @return the articleTags
     */
    public String getArticleTags() {
        return articleTags;
    }

    /**
     * @param articleTags
     *            the articleTags to set
     */
    public void setArticleTags(String articleTags) {
        this.articleTags = articleTags;
    }

    /**
     * @return the articleDescription
     */
    public String getArticleDescription() {
        return articleDescription;
    }

    /**
     * @param articleDescription
     *            the articleDescription to set
     */
    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    /**
     * @return the articleImage
     */
    public String getArticleImage() {
        return articleImage;
    }

    /**
     * @param articleImage
     *            the articleImage to set
     */
    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }

    /**
     * @return the articleType
     */
    public Integer getArticleType() {
        return articleType;
    }

    /**
     * @param articleType
     *            the articleType to set
     */
    public void setArticleType(Integer articleType) {
        this.articleType = articleType;
    }

    /**
     * @return the articleSource
     */
    public String getArticleSource() {
        return articleSource;
    }

    /**
     * @param articleSource
     *            the articleSource to set
     */
    public void setArticleSource(String articleSource) {
        this.articleSource = articleSource;
    }

    /**
     * @return the articleLink
     */
    public String getArticleLink() {
        return articleLink;
    }

    /**
     * @param articleLink
     *            the articleLink to set
     */
    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    /**
     * @return the columnId
     */
    public Integer getColumnId() {
        return columnId;
    }

    /**
     * @param columnId
     *            the columnId to set
     */
    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    /**
     * @return the secondColumn
     */
    public Integer getSecondColumn() {
        return secondColumn;
    }

    /**
     * @param secondColumn
     *            the secondColumn to set
     */
    public void setSecondColumn(Integer secondColumn) {
        this.secondColumn = secondColumn;
    }

    /**
     * @return the displayStatus
     */
    public long getDisplayStatus() {
        return displayStatus;
    }

    /**
     * @param displayStatus
     *            the displayStatus to set
     */
    public void setDisplayStatus(long displayStatus) {
        this.displayStatus = displayStatus;
    }

    /**
     * @return the displayOrder
     */
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    /**
     * @param displayOrder
     *            the displayOrder to set
     */
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    /**
     * @return the commentSet
     */
    public long getCommentSet() {
        return commentSet;
    }

    /**
     * @param commentSet
     *            the commentSet to set
     */
    public void setCommentSet(long commentSet) {
        this.commentSet = commentSet;
    }

    /**
     * @return the publishSet
     */
    public Boolean getPublishSet() {
        return publishSet;
    }

    /**
     * @param publishSet
     *            the publishSet to set
     */
    public void setPublishSet(Boolean publishSet) {
        this.publishSet = publishSet;
    }

    /**
     * @return the publishTime
     */
    public Date getPublishTime() {
        return publishTime;
    }

    /**
     * @param publishTime
     *            the publishTime to set
     */
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    /**
     * @return the publishStatus
     */
    public Integer getPublishStatus() {
        return publishStatus;
    }

    /**
     * @param publishStatus
     *            the publishStatus to set
     */
    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    /**
     * @return the createUser
     */
    public Long getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser
     *            the createUser to set
     */
    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     *            the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     *            the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the updateUser
     */
    public Long getUpdateUser() {
        return updateUser;
    }

    /**
     * @param updateUser
     *            the updateUser to set
     */
    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return the auditUser
     */
    public Integer getAuditUser() {
        return auditUser;
    }

    /**
     * @param auditUser
     *            the auditUser to set
     */
    public void setAuditUser(Integer auditUser) {
        this.auditUser = auditUser;
    }

    /**
     * @return the auditDate
     */
    public Date getAuditDate() {
        return auditDate;
    }

    /**
     * @param auditDate
     *            the auditDate to set
     */
    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    /**
     * @return the sysUser
     */
    public Integer getSysUser() {
        return sysUser;
    }

    /**
     * @param sysUser
     *            the sysUser to set
     */
    public void setSysUser(Integer sysUser) {
        this.sysUser = sysUser;
    }

    /**
     * @return the refuseReason
     */
    public String getRefuseReason() {
        return refuseReason;
    }

    /**
     * @param refuseReason
     *            the refuseReason to set
     */
    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    /**
     * @return the bowseNum
     */
    public Integer getBowseNum() {
        return bowseNum;
    }

    /**
     * @param bowseNum
     *            the bowseNum to set
     */
    public void setBowseNum(Integer bowseNum) {
        this.bowseNum = bowseNum;
    }

    public Integer getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(Integer browseNum) {
        this.browseNum = browseNum;
    }

    /**
     * @return the articleText
     */
    public String getArticleText() {
        return articleText;
    }

    /**
     * @param articleText
     *            the articleText to set
     */
    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    /**
     * @return the collect
     */
    public int getCollect() {
        return collect;
    }

    /**
     * @param collect
     *            the collect to set
     */
    public void setCollect(int collect) {
        this.collect = collect;
    }

    /**
     * @return the comments
     */
    public int getComments() {
        return comments;
    }

    /**
     * @param comments
     *            the comments to set
     */
    public void setComments(int comments) {
        this.comments = comments;
    }

    /**
     * @return the profilename
     */
    public String getProfilename() {
        return profilename;
    }

    /**
     * @param profilename
     *            the profilename to set
     */
    public void setProfilename(String profilename) {
        this.profilename = profilename;
    }

    /**
     * @return the profileavatar
     */
    public String getProfileavatar() {
        return profileavatar;
    }

    /**
     * @param profileavatar
     *            the profileavatar to set
     */
    public void setProfileavatar(String profileavatar) {
        this.profileavatar = profileavatar;
    }

    public ArticleOutModel() {
        super();
    }

    /**
     * @return the articleTagnames
     */
    public String getArticleTagnames() {
        return articleTagnames;
    }

    /**
     * @param articleTagnames
     *            the articleTagnames to set
     */
    public void setArticleTagnames(String articleTagnames) {
        this.articleTagnames = articleTagnames;
    }

    public String getUserColumn() {
        return userColumn;
    }

    public void setUserColumn(String userColumn) {
        this.userColumn = userColumn;
    }

    public String getColumnAvatar() {
        return columnAvatar;
    }

    public void setColumnAvatar(String columnAvatar) {
        this.columnAvatar = columnAvatar;
    }

    public int getIsPublishBonus() {
        return isPublishBonus;
    }

    public void setIsPublishBonus(int isPublishBonus) {
        this.isPublishBonus = isPublishBonus;
    }

    public int getArticleMark() {
        return articleMark;
    }

    public void setArticleMark(int articleMark) {
        this.articleMark = articleMark;
    }

    public int getPublishBonusStatus() {
        return publishBonusStatus;
    }

    public void setPublishBonusStatus(int publishBonusStatus) {
        this.publishBonusStatus = publishBonusStatus;
    }

    public String getFreezeReason() {
        return freezeReason;
    }

    public void setFreezeReason(String freezeReason) {
        this.freezeReason = freezeReason;
    }

    public int getCollectStatusInfo() {
        return collectStatusInfo;
    }

    public void setCollectStatusInfo(int collectStatusInfo) {
        this.collectStatusInfo = collectStatusInfo;
    }
}