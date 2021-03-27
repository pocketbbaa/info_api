package com.kg.platform.model.in;

import java.util.Date;
import java.util.List;

public class ArticleInModel {

    private String articleTagnames;

    private int limit;

    private int start;

    private Long articleId;

    private String articleTitle;

    private String articleTags;

    private String articleDescription;

    private String articleImage;

    private Integer articleType;

    private Integer days;

    private String articleSource;

    private String articleLink;

    private String columnId;

    private String addedBonusReason;

    private Integer secondColumn;

    private Integer displayStatus;

    private Integer displayOrder;

    private Boolean commentSet;

    private Boolean publishSet;

    private Date publishTime;

    private String publishStatus;

    private Long createUser;

    private Date createDate;

    private Date updateDate;

    private Long updateUser;

    private Integer sysUser;

    private String refuseReason;

    private String articleText;

    private String userId;

    private Integer articleFrom;// 来源 1 本站 2 btc123

    private Integer bonusStatus;

    private int auditUser;

    private Date auditDate;

    private int bowseNum;

    private String tagnames;

    private String state;

    private Long textnum;

    private String searchword;

    private Integer publishKind; // 发布类型 1 文章 2 视频

    private String videoUrl;

    private String videoFilename;

    private int totalCount; // 查询数量

    private Integer ifPlatformPublishAward;// 是否设有奖励 0：否 1：是

    private Integer ifPush;// 是否推送 0：否 1：是

    private List<Long> authorIds;

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

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
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
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state
     *            the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the tagnames
     */
    public String getTagnames() {
        return tagnames;
    }

    /**
     * @param tagnames
     *            the tagnames to set
     */
    public void setTagnames(String tagnames) {
        this.tagnames = tagnames;
    }

    /**
     * @return the bowseNum
     */
    public int getBowseNum() {
        return bowseNum;
    }

    /**
     * @param bowseNum
     *            the bowseNum to set
     */
    public void setBowseNum(int bowseNum) {
        this.bowseNum = bowseNum;
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
     * @return the auditUser
     */
    public int getAuditUser() {
        return auditUser;
    }

    /**
     * @param auditUser
     *            the auditUser to set
     */
    public void setAuditUser(int auditUser) {
        this.auditUser = auditUser;
    }

    /**
     * @return the articleFrom
     */
    public Integer getArticleFrom() {
        return articleFrom;
    }

    /**
     * @param articleFrom
     *            the articleFrom to set
     */
    public void setArticleFrom(Integer articleFrom) {
        this.articleFrom = articleFrom;
    }

    /**
     * @return the bonusStatus
     */
    public Integer getBonusStatus() {
        return bonusStatus;
    }

    /**
     * @param bonusStatus
     *            the bonusStatus to set
     */
    public void setBonusStatus(Integer bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
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

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
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

    public Boolean getPublishSet() {
        return publishSet;
    }

    public void setPublishSet(Boolean publishSet) {
        this.publishSet = publishSet;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public String getAddedBonusReason() {
        return addedBonusReason;
    }

    public void setAddedBonusReason(String addedBonusReason) {
        this.addedBonusReason = addedBonusReason;
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

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
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

    public ArticleInModel() {
        super();
    }

    /**
     * @return the limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * @param limit
     *            the limit to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start
     *            the start to set
     */
    public void setStart(int start) {
        this.start = start;
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

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public List<Long> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(List<Long> authorIds) {
        this.authorIds = authorIds;
    }

}