package com.kg.platform.model.out;

import java.math.BigDecimal;

public class KgArticleStatisticsOutModel {
    private Long articleId;

    private Integer browseNum;

    private Integer shareNum;

    private Integer thumbupNum;

    private Integer collectNum;

    private Integer bonusNum;

    private BigDecimal bonusValue;

    private BigDecimal bonusTotal;

    private Long createUser;

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
}