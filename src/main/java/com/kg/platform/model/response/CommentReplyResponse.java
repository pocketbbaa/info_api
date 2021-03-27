package com.kg.platform.model.response;

import com.kg.platform.model.out.CommentReplyOutModel;

import java.util.Map;

/**
 * 子回复
 */
public class CommentReplyResponse {

    private String commentId;

    private String parentCommentId;

    private CommentUser commentUser;

    private CommentUser replyUser;

    private String commentDesc;

    private String commentDate;

    private int displayStatus;  //0 隐藏 1 显示 2 删除

    private int floorState = 2;  //1:主评论  2:子回复

    public int getFloorState() {
        return floorState;
    }

    public void setFloorState(int floorState) {
        this.floorState = floorState;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
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

    public static CommentReplyResponse build(CommentReplyOutModel outModel, Map<Long, CommentUser> commentUserMap) {
        CommentReplyResponse replyResponse = new CommentReplyResponse();
        replyResponse.setCommentId(String.valueOf(outModel.getCommentId()));
        replyResponse.setCommentDesc(outModel.getCommentDesc());
        replyResponse.setCommentDate(String.valueOf(outModel.getCommentDate().getTime()));
        replyResponse.setDisplayStatus(outModel.getDisplayStatus());
        replyResponse.setCommentUser(commentUserMap.get(outModel.getUserId()));
        Long toUserId = outModel.getToUserId();
        if (toUserId != null && toUserId > 0) {
            replyResponse.setReplyUser(commentUserMap.get(outModel.getToUserId()));
        }
        return replyResponse;
    }
}
