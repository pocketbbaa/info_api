package com.kg.platform.model.request;

import java.io.Serializable;
import java.util.Date;

public class UserCollectRequest extends BaseRequest implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 4059383894365415572L;

    private Long collectId;

    private Long userId;

    private Long articleId;

    private Date collectDate;

    private int currentPage;

    private Integer operType;

    private Integer source;

    private Integer osVersion;

    private String deviceId;

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

    public Integer getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(Integer osVersion) {
        this.osVersion = osVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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
     * @return the currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage
     *            the currentPage to set
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

}