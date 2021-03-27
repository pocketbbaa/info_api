package com.kg.platform.model.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ArticleResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5792499177876973080L;

    private ArticleResponse OnResponse;

    private ArticleResponse Underesponse;

    private String username;

    private String articleTagnames;

    private String bonusNum;// 奖励领取人数

    private String tipsinCount;// 文章被打赏数

    private String profilename;// 专栏名称，热门文章接口用于文章所在的栏目展示

    private String profileavatar;// 专栏头像

    private String columnname;// 作者栏目名称

    private String columnintro;// 专栏介绍

    private long collectstatus;// 收藏状态

    private long articlefrom;// 来源

    private UserProfileResponse profileResponse;

    private String articleId;

    private String articleTitle;

    private String articleTags;

    private String articleDescription;

    private String articleImage;

    private Integer articleType;

    private String articleSource;

    private String articleLink;

    private Integer columnId;

    private Integer isPublishBonus;

    private Integer articleMark;

    private Integer publishBonusStatus;

    private String freezeReason;

    private Integer secondColumn;

    private String displayStatus;

    private Integer displayOrder;

    private long commentSet;

    private Boolean publishSet;

    private Date publishTime;

    private Integer publishStatus;

    private String createUser;

    private Date createDate;

    private String updateDate;

    private Long updateUser;

    private Integer auditUser;

    private Date auditDate;

    private Integer sysUser;

    private String refuseReason;

    private Integer bowseNum;

    private String articleText;

    private String secondcolumnname;

    private String userColumn;

    private String columnAvatar;

    private int bonusStatus;// 是否开启打赏

    private long bonus;

    private long praisestatus;// 用户是否点赞过 0：未点赞

    private int bonusState;// 阅读奖励是否生效

    private String bonusValuesum;

    private int getStatus;// 领取浏览奖励状态

    private Integer browseNum;// 文章浏览数

    private Integer thumbupNum;// 点赞数

    private Integer collect;// 收藏数

    private Integer comments;// 评论数

    private List<KgArticleBonusResponse> listArtBonus;

    private List<KgArticleBonusResponse> listAllBonus;

    private Long textnum;

    private String searchword;

    private Integer commentAudit;

    private Integer publishKind; // 发布类型 1 文章 2 视频

    private String videoUrl;

    private String videoFilename;

    private String updateTimestamp;

    private Integer articleImgSize;//1：小图  2：大图

    private String rewardIncome;//作者奖励收入

    private String blockchainUrl; //文章上链后的查看地址

	private KgSeoTdkResponse seoTdk;

	public KgSeoTdkResponse getSeoTdk() {
		return seoTdk;
	}

	public void setSeoTdk(KgSeoTdkResponse seoTdk) {
		this.seoTdk = seoTdk;
	}

	public String getBlockchainUrl() {
        return blockchainUrl;
    }

    public void setBlockchainUrl(String blockchainUrl) {
        this.blockchainUrl = blockchainUrl;
    }

    public String getRewardIncome() {
        return rewardIncome;
    }

    public void setRewardIncome(String rewardIncome) {
        this.rewardIncome = rewardIncome;
    }

    public Integer getArticleImgSize() {
        return articleImgSize;
    }

    public void setArticleImgSize(Integer articleImgSize) {
        this.articleImgSize = articleImgSize;
    }

    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
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

    public List<KgArticleBonusResponse> getListAllBonus() {
        return listAllBonus;
    }

    public void setListAllBonus(List<KgArticleBonusResponse> listAllBonus) {
        this.listAllBonus = listAllBonus;
    }

    public Integer getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(Integer browseNum) {
        this.browseNum = browseNum;
    }

    public Integer getThumbupNum() {
        return thumbupNum;
    }

    public void setThumbupNum(Integer thumbupNum) {
        this.thumbupNum = thumbupNum;
    }

    public Integer getCollect() {
        return collect;
    }

    public void setCollect(Integer collect) {
        this.collect = collect;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
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
     * @return the bonusValuesum
     */
    public String getBonusValuesum() {
        return bonusValuesum;
    }

    /**
     * @param bonusValuesum
     *            the bonusValuesum to set
     */
    public void setBonusValuesum(String bonusValuesum) {
        this.bonusValuesum = bonusValuesum;
    }

    /**
     * @return the listArtBonus
     */
    public List<KgArticleBonusResponse> getListArtBonus() {
        return listArtBonus;
    }

    /**
     * @param listArtBonus
     *            the listArtBonus to set
     */
    public void setListArtBonus(List<KgArticleBonusResponse> listArtBonus) {
        this.listArtBonus = listArtBonus;
    }

    /**
     * @return the bonusState
     */
    public int getBonusState() {
        return bonusState;
    }

    /**
     * @param bonusState
     *            the bonusState to set
     */
    public void setBonusState(int bonusState) {
        this.bonusState = bonusState;
    }

    /**
     * @return the tipsinCount
     */
    public String getTipsinCount() {
        return tipsinCount;
    }

    /**
     * @param tipsinCount
     *            the tipsinCount to set
     */
    public void setTipsinCount(String tipsinCount) {
        this.tipsinCount = tipsinCount;
    }

    /**
     * @return the bonusNum
     */
    public String getBonusNum() {
        return bonusNum;
    }

    /**
     * @param bonusNum
     *            the bonusNum to set
     */
    public void setBonusNum(String bonusNum) {
        this.bonusNum = bonusNum;
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
     * @return the onResponse
     */
    public ArticleResponse getOnResponse() {
        return OnResponse;
    }

    /**
     * @param onResponse
     *            the onResponse to set
     */
    public void setOnResponse(ArticleResponse onResponse) {
        OnResponse = onResponse;
    }

    /**
     * @return the underesponse
     */
    public ArticleResponse getUnderesponse() {
        return Underesponse;
    }

    /**
     * @param underesponse
     *            the underesponse to set
     */
    public void setUnderesponse(ArticleResponse underesponse) {
        Underesponse = underesponse;
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
     * @return the profileResponse
     */
    public UserProfileResponse getProfileResponse() {
        return profileResponse;
    }

    /**
     * @param profileResponse
     *            the profileResponse to set
     */
    public void setProfileResponse(UserProfileResponse profileResponse) {
        this.profileResponse = profileResponse;
    }

    /**
     * @return the columnintro
     */
    public String getColumnintro() {
        return columnintro;
    }

    /**
     * @param columnintro
     *            the columnintro to set
     */
    public void setColumnintro(String columnintro) {
        this.columnintro = columnintro;
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
    public String getDisplayStatus() {
        return displayStatus;
    }

    /**
     * @param displayStatus
     *            the displayStatus to set
     */
    public void setDisplayStatus(String displayStatus) {
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
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser
     *            the createUser to set
     */
    public void setCreateUser(String createUser) {
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
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     *            the updateDate to set
     */
    public void setUpdateDate(String updateDate) {
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

    public ArticleResponse() {
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

    public Integer getCommentAudit() {
        return commentAudit;
    }

    public void setCommentAudit(Integer commentAudit) {
        this.commentAudit = commentAudit;
    }

    public Integer getIsPublishBonus() {
        return isPublishBonus;
    }

    public void setIsPublishBonus(Integer isPublishBonus) {
        this.isPublishBonus = isPublishBonus;
    }

    public Integer getArticleMark() {
        return articleMark;
    }

    public void setArticleMark(Integer articleMark) {
        this.articleMark = articleMark;
    }

    public Integer getPublishBonusStatus() {
        return publishBonusStatus;
    }

    public void setPublishBonusStatus(Integer publishBonusStatus) {
        this.publishBonusStatus = publishBonusStatus;
    }

    public String getFreezeReason() {
        return freezeReason;
    }

    public void setFreezeReason(String freezeReason) {
        this.freezeReason = freezeReason;
    }

}
