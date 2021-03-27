package com.kg.platform.dao.entity;

import java.util.Date;

public class UserCollect {
    private Long collectId;

    private Long userId;

    private Long articleId;

    private Integer operType;

    private Date collectDate;

    private Integer source;

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
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

    public Date getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(Date collectDate) {
        this.collectDate = collectDate;
    }

    /**
     * @return the operType
     */
    public Integer getOperType() {
        return operType;
    }

    /**
     * @param operType
     *            the operType to set
     */
    public void setOperType(Integer operType) {
        this.operType = operType;
    }

}