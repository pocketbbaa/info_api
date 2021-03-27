package com.kg.platform.model.response;

import java.io.Serializable;

public class UserCommentResponse implements Serializable{
    private String commentId;

    private String userId;

    private String articleId;

    private String parentCommentId;

    private String commentDesc;

    private String commentDate;

    private Integer commentStatus;

    private String refuseReason;

    private String displayStatus;

    private String articleImage;

    private String articleTitle;

    private String userName;

    private String avatar;

    private String commentDateTimestamp;

    private Integer publishKind;

    private Integer articleImageSize;

    /**V1.0.1新增字段**/
    private String identityTag; //身份标识
    private int realAuthedTag; //实名标签 0:无，1：有
    private int vipTag; //是否有大V标签 0：无，1：有

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

	public String getIdentityTag() {
        return identityTag;
    }

    public void setIdentityTag(String identityTag) {
        this.identityTag = identityTag;
    }

    public int getRealAuthedTag() {
        return realAuthedTag;
    }

    public void setRealAuthedTag(int realAuthedTag) {
        this.realAuthedTag = realAuthedTag;
    }

    public int getVipTag() {
        return vipTag;
    }

    public void setVipTag(int vipTag) {
        this.vipTag = vipTag;
    }

    public Integer getPublishKind() {
        return publishKind;
    }

    public void setPublishKind(Integer publishKind) {
        this.publishKind = publishKind;
    }

    public String getCommentDateTimestamp() {
        return commentDateTimestamp;
    }

    public void setCommentDateTimestamp(String commentDateTimestamp) {
        this.commentDateTimestamp = commentDateTimestamp;
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
     * @return the avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * @param avatar
     *            the avatar to set
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public String getCommentDesc() {
        return commentDesc;
    }

    public void setCommentDesc(String commentDesc) {
        this.commentDesc = commentDesc;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
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