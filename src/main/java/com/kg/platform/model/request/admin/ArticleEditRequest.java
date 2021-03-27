package com.kg.platform.model.request.admin;

import java.util.Date;

/**
 * 文章发布/编辑入参
 * 文章ID、标题、正文、Tag标签、摘要、封面图片、类别（原创、转载）、栏目、显示设置、排序、评论设置、定时发布、定时发布时间、发布人账号（手机号或邮箱）
 */
public class ArticleEditRequest extends AdminBaseRequest {

    /**
     *
     */
    private static final long serialVersionUID = 3547582053232729509L;

    private Long articleId;

    private String articleTitle;

    private String articleText;

    private String articleTag;

    private String description;

    private String image;

    // 类别（1、原创2、转载）
    private Integer type;

    private Integer columnId;

    private Integer secondColumn;

    private Integer displayStatus;

    private Integer displayOrder;

    private Boolean commentSet;

    private Integer publishSet;

    private Date publishTime;

    private String createUser;

    private Integer sysUser;

    private String tagnames;

    private Integer bonusStatus;

    private Integer publishStatus;

    private String refuseReason;

    private Integer textnum;

    private String articleSource;

    private String articleLink;

    private Integer browseNum;

    private Integer shareNum;

    private Integer thumbupNum;

    private Integer collectNum;

    private Integer editArticle;

    private Integer publishKind; // 发布类型 1 文章 2 视频

    private String videoUrl;

    private String videoFilename;

    private Integer ifPlatformPublishAward;//是否设有奖励 0：否 1：是

    private Integer ifPush;//是否推送 0：否 1：是

    private Long auditUser;

    private Integer articleImgSize;//1：小图  2：大图

    private Integer ifIntoIndex;//是否展示到首页推荐列表 0：否 1：是


    /**
     * 1.3.8新增推送自定义
     */
    private String pushTitle;
    private String pushMessage;

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    public String getPushMessage() {
        return pushMessage;
    }

    public void setPushMessage(String pushMessage) {
        this.pushMessage = pushMessage;
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

    public Long getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(Long auditUser) {
        this.auditUser = auditUser;
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

    public Integer getEditArticle() {
        return editArticle;
    }

    public void setEditArticle(Integer editArticle) {
        this.editArticle = editArticle;
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

    public Integer getTextnum() {
        return textnum;
    }

    public void setTextnum(Integer textnum) {
        this.textnum = textnum;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
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

    public Integer getSysUser() {
        return sysUser;
    }

    public void setSysUser(Integer sysUser) {
        this.sysUser = sysUser;
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

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    public String getArticleTag() {
        return articleTag;
    }

    public void setArticleTag(String articleTag) {
        this.articleTag = articleTag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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

}
