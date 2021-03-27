package com.kg.platform.model.request.admin;

import java.util.Date;
import java.util.List;

public class CommentQueryRequest extends AdminBaseRequest {

    /**
     *
     */
    private static final long serialVersionUID = 3254002490301958019L;

    private String content;

    private Date startDate;

    private Date endDate;

    private Integer displayStatus;

    private Integer status;

    private Long commentId;

    private String commentIds;

    private String refuseReason;

    private Boolean commentSet;

    private String articleTitle;

    private String commentUser;

    private String userMobile;

    private int floorState;  //1:主评论  2:子回复

    private List<CommentBatchRequest> list;

    public List<CommentBatchRequest> getList() {
        return list;
    }

    public void setList(List<CommentBatchRequest> list) {
        this.list = list;
    }

    public int getFloorState() {
        return floorState;
    }

    public void setFloorState(int floorState) {
        this.floorState = floorState;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(String commentUser) {
        this.commentUser = commentUser;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public Boolean getCommentSet() {
        return commentSet;
    }

    public void setCommentSet(Boolean commentSet) {
        this.commentSet = commentSet;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(String commentIds) {
        this.commentIds = commentIds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(Integer displayStatus) {
        this.displayStatus = displayStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
