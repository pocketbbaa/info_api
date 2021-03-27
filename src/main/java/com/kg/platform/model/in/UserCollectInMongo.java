package com.kg.platform.model.in;

import java.util.Date;

public class UserCollectInMongo {

    private Long collectId;

    private Long userId;

    private Long articleId;

    private Integer operType;

    private Integer source;

    private Date collectDate;

    private Long time;

    private Integer publishKind;

    public Integer getPublishKind() {
        return publishKind;
    }

    public void setPublishKind(Integer publishKind) {
        this.publishKind = publishKind;
    }

    public Long getCollectId() {
        return collectId;
    }

    public void setCollectId(Long collectId) {
        this.collectId = collectId;
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

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Date getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(Date collectDate) {
        this.collectDate = collectDate;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
