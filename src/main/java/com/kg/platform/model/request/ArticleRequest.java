package com.kg.platform.model.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ArticleRequest extends BaseRequest implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5792499177876973080L;

    private List<KgArticleBonusRequest> list;

    private int currentPage;

    private Integer pageSize;

    private Long articleId;

    private String articleTagnames;

    private String articleTitle;

    private String articleTags;

    private String articleDescription;

    private String articleImage;

    private Integer articleType;

    private String articleSource;

    private String articleLink;

    private String columnId;

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

    private Integer updateUser;

    private Integer sysUser;

    private String refuseReason;

    private String articleText;

    private String userId;

    private Integer articleFrom; // 来源 1 本站 2 btc123

    private Integer bonusStatus; // 是否开启打赏 0 禁用 1 启用

    private Long textnum;

    private Integer publishKind; // 发布类型 1 文章 2 视频

    private String videoUrl;

    private String videoFilename;

    private String userIp;

    private Integer type;//1 对应旷视的千氪视频栏目 2.对应旷视的千氪资讯栏目 3 对应旷视精选栏目

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
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

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTextnum() {
        return textnum;
    }

    public void setTextnum(Long textnum) {
        this.textnum = textnum;
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

    /**
     * @return the currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage
     *            the currentPage to set
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
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

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
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

    /**
     * @return the list
     */
    public List<KgArticleBonusRequest> getList() {
        return list;
    }

    /**
     * @param list
     *            the list to set
     */
    public void setList(List<KgArticleBonusRequest> list) {
        this.list = list;
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

    public ArticleRequest(List<KgArticleBonusRequest> list, int currentPage, Long articleId, String articleTagnames,
            String articleTitle, String articleTags, String articleDescription, String articleImage,
            Integer articleType, String articleSource, String articleLink, String columnId, Integer secondColumn,
            Integer displayStatus, Integer displayOrder, Boolean commentSet, Boolean publishSet, Date publishTime,
            String publishStatus, Long createUser, Date createDate, Date updateDate, Integer updateUser,
            Integer sysUser, String refuseReason, String articleText, String userId, Integer articleFrom,
            Integer bonusStatus) {
        super();
        this.list = list;
        this.currentPage = currentPage;
        this.articleId = articleId;
        this.articleTagnames = articleTagnames;
        this.articleTitle = articleTitle;
        this.articleTags = articleTags;
        this.articleDescription = articleDescription;
        this.articleImage = articleImage;
        this.articleType = articleType;
        this.articleSource = articleSource;
        this.articleLink = articleLink;
        this.columnId = columnId;
        this.secondColumn = secondColumn;
        this.displayStatus = displayStatus;
        this.displayOrder = displayOrder;
        this.commentSet = commentSet;
        this.publishSet = publishSet;
        this.publishTime = publishTime;
        this.publishStatus = publishStatus;
        this.createUser = createUser;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.updateUser = updateUser;
        this.sysUser = sysUser;
        this.refuseReason = refuseReason;
        this.articleText = articleText;
        this.userId = userId;
        this.articleFrom = articleFrom;
        this.bonusStatus = bonusStatus;
    }

    public ArticleRequest() {
        super();
    }

}