package com.kg.platform.model.in;

import java.math.BigDecimal;

public class KgArticleStatisticsInModel {
    private Long articleId;

    private Integer browseNum;

    private Integer shareNum;

    private Integer thumbupNum;

    private Integer collectNum;

    private Integer bonusNum;

    private BigDecimal bonusValue;

    private BigDecimal bonusTotal;

    private Long createUser;

    // 1 添加浏览量 2.发文奖励浏览奖励
    private Integer types;

    private String deviceId;

    private Integer osVersion;

    private Long userId;

    /**
     * @return the bonusTotal
     */
    public BigDecimal getBonusTotal() {
        return bonusTotal;
    }

    /**
     * @param bonusTotal
     *            the bonusTotal to set
     */
    public void setBonusTotal(BigDecimal bonusTotal) {
        this.bonusTotal = bonusTotal;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Integer getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(Integer browseNum) {
        this.browseNum = browseNum;
    }

    public Integer getShareNum() {
        return shareNum;
    }

    public void setShareNum(Integer shareNum) {
        this.shareNum = shareNum;
    }

    public Integer getThumbupNum() {
        return thumbupNum;
    }

    public void setThumbupNum(Integer thumbupNum) {
        this.thumbupNum = thumbupNum;
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public Integer getBonusNum() {
        return bonusNum;
    }

    public void setBonusNum(Integer bonusNum) {
        this.bonusNum = bonusNum;
    }

    public BigDecimal getBonusValue() {
        return bonusValue;
    }

    public void setBonusValue(BigDecimal bonusValue) {
        this.bonusValue = bonusValue;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(Integer osVersion) {
        this.osVersion = osVersion;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}