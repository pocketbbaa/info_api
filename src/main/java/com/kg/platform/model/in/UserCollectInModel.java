package com.kg.platform.model.in;

import java.util.Date;

public class UserCollectInModel {
    private Long collectId;

    private Long userId;

    private Long articleId;

    private Date collectDate;

    private Integer operType;

    private int limit;

    private int start;

    private String deviceId;

    private Integer source;

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
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
     * @return the limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * @param limit
     *            the limit to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start
     *            the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }

    public UserCollectInModel() {
        super();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}