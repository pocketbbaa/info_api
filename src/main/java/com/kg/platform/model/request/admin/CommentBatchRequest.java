package com.kg.platform.model.request.admin;

public class CommentBatchRequest {

    private Long commentId;
    private int floorState;  //1:主评论  2:子回复

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public int getFloorState() {
        return floorState;
    }

    public void setFloorState(int floorState) {
        this.floorState = floorState;
    }
}
