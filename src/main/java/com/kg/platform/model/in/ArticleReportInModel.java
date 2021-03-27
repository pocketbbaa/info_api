package com.kg.platform.model.in;

import java.util.Date;

public class ArticleReportInModel {

    private Long id;
    private Long articleId;
    private Long reportTextId;
    private Long userId;
    private int state;
    private Date createTime;
    private Date updateTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getReportTextId() {
        return reportTextId;
    }

    public void setReportTextId(Long reportTextId) {
        this.reportTextId = reportTextId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
