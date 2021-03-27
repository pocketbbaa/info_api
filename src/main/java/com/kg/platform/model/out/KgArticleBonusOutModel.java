package com.kg.platform.model.out;

import java.math.BigDecimal;
import java.util.Date;

public class KgArticleBonusOutModel {
    private Long bonusId;

    private Long articleId;

    private Integer bonusType;

    private Integer bonusSecondType;

    private Integer bonusKind;

    private Integer browseTime;

    private BigDecimal bonusValue;

    private Integer maxPeople;

    private Integer bonusStatus;

    private Long createUser;

    private Date createDate;

    private Date updateDate;

    private Long updateUser;

    private String bonusTypeid;

    private String bonusTypename;

    /**
     * @return the bonusTypeid
     */
    public String getBonusTypeid() {
        return bonusTypeid;
    }

    /**
     * @param bonusTypeid
     *            the bonusTypeid to set
     */
    public void setBonusTypeid(String bonusTypeid) {
        this.bonusTypeid = bonusTypeid;
    }

    /**
     * @return the bonusTypename
     */
    public String getBonusTypename() {
        return bonusTypename;
    }

    /**
     * @param bonusTypename
     *            the bonusTypename to set
     */
    public void setBonusTypename(String bonusTypename) {
        this.bonusTypename = bonusTypename;
    }

    public Integer getBonusType() {
        return bonusType;
    }

    public void setBonusType(Integer bonusType) {
        this.bonusType = bonusType;
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

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
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

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
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

    public Integer getBonusSecondType() {
        return bonusSecondType;
    }

    public void setBonusSecondType(Integer bonusSecondType) {
        this.bonusSecondType = bonusSecondType;
    }

    public Integer getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(Integer bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    public Integer getBonusKind() {
        return bonusKind;
    }

    public void setBonusKind(Integer bonusKind) {
        this.bonusKind = bonusKind;
    }

}