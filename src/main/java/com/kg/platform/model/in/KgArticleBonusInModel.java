package com.kg.platform.model.in;

import java.math.BigDecimal;
import java.util.Date;

public class KgArticleBonusInModel {
    private Long bonusId;

    private Long articleId;

    private String bonusType;

    private String bonusSecondType;

    private Integer bonusKind;

    private Integer browseTime;

    private BigDecimal bonusValue;

    private Integer maxPeople;

    private Integer bonusStatus;

    private Long createUser;

    private Date createDate;

    private Date updateDate;

    private Long updateUser;

    private BigDecimal bonusValuesum;// 钛值统计

    /**
     * @return the bonusValuesum
     */
    public BigDecimal getBonusValuesum() {
        return bonusValuesum;
    }

    /**
     * @param bonusValuesum
     *            the bonusValuesum to set
     */
    public void setBonusValuesum(BigDecimal bonusValuesum) {
        this.bonusValuesum = bonusValuesum;
    }

    public Integer getBrowseTime() {
        return browseTime;
    }

    public void setBrowseTime(Integer browseTime) {
        this.browseTime = browseTime;
    }

    public BigDecimal getBonusValue() {
        return bonusValue;
    }

    public void setBonusValue(BigDecimal bonusValue) {
        this.bonusValue = bonusValue;
    }

    public Integer getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(Integer maxPeople) {
        this.maxPeople = maxPeople;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getBonusId() {
        return bonusId;
    }

    public void setBonusId(Long bonusId) {
        this.bonusId = bonusId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getBonusType() {
        return bonusType;
    }

    public void setBonusType(String bonusType) {
        this.bonusType = bonusType;
    }

    public String getBonusSecondType() {
        return bonusSecondType;
    }

    public void setBonusSecondType(String bonusSecondType) {
        this.bonusSecondType = bonusSecondType;
    }

    public Integer getBonusKind() {
        return bonusKind;
    }

    public void setBonusKind(Integer bonusKind) {
        this.bonusKind = bonusKind;
    }

    public Integer getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(Integer bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

}
