package com.kg.platform.model.out;

import java.util.Date;

public class UserCommentOutModel {
    private Long commentId;

    private Long userId;

    private Long articleId;

    private Long parentCommentId;

    private String commentDesc;

    private Date commentDate;

    private Integer commentStatus;

    private String refuseReason;

    private String displayStatus;

    private String articleImage;

    private String articleTitle;

    private String userName;

    private String avaTar;

    private Integer publishKind;

    private String videoFilename;

    private Long createUser;

    private Integer articleImageSize;

    private Integer userRole;

	public Integer getUserRole() {
		return userRole;
	}

	public void setUserRole(Integer userRole) {
		this.userRole = userRole;
	}

	public Integer getArticleImageSize() {
		return articleImageSize;
	}

	public void setArticleImageSize(Integer articleImageSize) {
		this.articleImageSize = articleImageSize;
	}

	public String getVideoFilename() {
        return videoFilename;
    }

    public void setVideoFilename(String videoFilename) {
        this.videoFilename = videoFilename;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Integer getPublishKind() {
        return publishKind;
    }

    public void setPublishKind(Integer publishKind) {
        this.publishKind = publishKind;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the avaTar
     */
    public String getAvaTar() {
        return avaTar;
    }

    /**
     * @param avaTar
     *            the avaTar to set
     */
    public void setAvaTar(String avaTar) {
        this.avaTar = avaTar;
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

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public String getCommentDesc() {
        return commentDesc;
    }

    public void setCommentDesc(String commentDesc) {
        this.commentDesc = commentDesc;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public Integer getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(Integer commentStatus) {
        this.commentStatus = commentStatus;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }
}