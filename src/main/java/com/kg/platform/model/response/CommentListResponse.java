package com.kg.platform.model.response;

import com.kg.platform.model.out.CommentReplyOutModel;

public class CommentListResponse {

    private String commentId;

    private String commentDesc;

    private String commentDate;

    private int displayStatus;  //0 隐藏 1 显示 2 删除

    private int floorState = 1;  //1:主评论  2:子回复

    private CommentUser commentUser;  //评论用户

    private CommentUser replyUser;  //回复用户

    private CommentArticle commentArticle;  //文章信息

    private ParentComment parentComment; //所属一级评论

    public ParentComment getParentComment() {
        return parentComment;
    }

    public void setParentComment(ParentComment parentComment) {
        this.parentComment = parentComment;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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

    public int getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(int displayStatus) {
        this.displayStatus = displayStatus;
    }

    public int getFloorState() {
        return floorState;
    }

    public void setFloorState(int floorState) {
        this.floorState = floorState;
    }

    public CommentUser getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(CommentUser commentUser) {
        this.commentUser = commentUser;
    }

    public CommentUser getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(CommentUser replyUser) {
        this.replyUser = replyUser;
    }

    public CommentArticle getCommentArticle() {
        return commentArticle;
    }

    public void setCommentArticle(CommentArticle commentArticle) {
        this.commentArticle = commentArticle;
    }

    /**
     * 组装基础数据
     *
     * @param outModel
     * @return
     */
    public static CommentListResponse initBaseInfo(CommentReplyOutModel outModel) {
        CommentListResponse response = new CommentListResponse();
        response.setCommentId(String.valueOf(outModel.getCommentId()));
        response.setCommentDesc(outModel.getCommentDesc());
        response.setCommentDate(String.valueOf(outModel.getCommentDate().getTime()));
        response.setFloorState(outModel.getFloorState());
        response.setDisplayStatus(outModel.getDisplayStatus());
        return response;
    }
}
