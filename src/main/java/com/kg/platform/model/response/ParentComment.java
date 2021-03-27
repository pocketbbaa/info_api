package com.kg.platform.model.response;

public class ParentComment {

    private String commentId;
    private String commentDesc;
    private String commentDate;
    private String articleId;
    private CommentUser commentUser;  //评论用户
    private int displayStatus;  //0 隐藏 1 显示 2 删除

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
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

    public CommentUser getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(CommentUser commentUser) {
        this.commentUser = commentUser;
    }

    public int getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(int displayStatus) {
        this.displayStatus = displayStatus;
    }
}
