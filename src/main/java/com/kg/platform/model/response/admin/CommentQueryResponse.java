package com.kg.platform.model.response.admin;

import java.util.Date;

public class CommentQueryResponse extends AdminBaseResponse {

    /**
     *
     */
    private static final long serialVersionUID = 5046207557500571581L;

    private String commentId;

    private String content;

    private String user;

    private String userMobile;

    private String userName;

    private Date createDate;

    private Integer status;

    private String statusDisplay;

    private int displayStatus;

    private String displayStatusDisplay;

    private String articleTitle;

    private String commentUserName;

    private String commentUserAvatar;

    private Long userId;

    private Long publishUserId;

    private int floorState;  //1:主评论  2:子回复

    private String replyUser; //回复用户

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public int getFloorState() {
        return floorState;
    }

    public void setFloorState(int floorState) {
        this.floorState = floorState;
    }

    public String getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(String replyUser) {
        this.replyUser = replyUser;
    }

    public Long getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(Long publishUserId) {
        this.publishUserId = publishUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public String getCommentUserAvatar() {
        return commentUserAvatar;
    }

    public void setCommentUserAvatar(String commentUserAvatar) {
        this.commentUserAvatar = commentUserAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public int getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(int displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatusDisplay() {
        return displayStatusDisplay;
    }

    public void setDisplayStatusDisplay(String displayStatusDisplay) {
        this.displayStatusDisplay = displayStatusDisplay;
    }
}
