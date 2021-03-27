package com.kg.platform.model.out;

import java.util.Date;

public class UserCollectOutModel {
    private String collectId;

    private String userId;

    private String articleId;

    private Date collectDate;

    private String articleImage;

    private String articleTitle;

    private String articleFrom;

    private Integer source;

    private Integer publishKind;

    private String videoFilename;

    private String createUser;

    private String userName;

    private String avatar;

    public String getVideoFilename() {
        return videoFilename;
    }

    public void setVideoFilename(String videoFilename) {
        this.videoFilename = videoFilename;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getPublishKind() {
        return publishKind;
    }

    public void setPublishKind(Integer publishKind) {
        this.publishKind = publishKind;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
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

    public String getCollectId() {
        return collectId;
    }

    public void setCollectId(String collectId) {
        this.collectId = collectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Date getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(Date collectDate) {
        this.collectDate = collectDate;
    }

    public String getArticleFrom() {
        return articleFrom;
    }

    public void setArticleFrom(String articleFrom) {
        this.articleFrom = articleFrom;
    }

}
