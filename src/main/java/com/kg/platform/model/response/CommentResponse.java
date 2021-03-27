package com.kg.platform.model.response;

import java.util.List;


public class CommentResponse {

    private String commentId;

    private Long userId;

    private CommentUser commentUser;

    private String commentDesc;

    private String commentDate;

    private String displayStatus;  //0 隐藏 1 显示 2 删除

    private List<CommentReplyResponse> commentReply;

    private int commentReplyCount;  //子回复总数

    private int floorState = 1;  //1:主评论  2:子回复


    public CommentResponse() {
    }

    public CommentResponse(String commentId, String commentDesc, String commentDate, String displayStatus, Long userId) {
        this.commentId = commentId;
        this.commentDesc = commentDesc;
        this.commentDate = commentDate;
        this.displayStatus = displayStatus;
        this.userId = userId;
    }

    public static CommentResponse initBaseInfo(String commentId, String commentDesc, String commentDate, String displayStatus, Long userId) {
        return new CommentResponse(commentId, commentDesc, commentDate, displayStatus, userId);
    }

    public int getFloorState() {
        return floorState;
    }

    public void setFloorState(int floorState) {
        this.floorState = floorState;
    }

    public int getCommentReplyCount() {
        return commentReplyCount;
    }

    public void setCommentReplyCount(int commentReplyCount) {
        this.commentReplyCount = commentReplyCount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public CommentUser getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(CommentUser commentUser) {
        this.commentUser = commentUser;
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

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public List<CommentReplyResponse> getCommentReply() {
        return commentReply;
    }

    public void setCommentReply(List<CommentReplyResponse> commentReply) {
        this.commentReply = commentReply;
    }
}