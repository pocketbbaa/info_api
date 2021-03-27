package com.kg.platform.model.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class KgArticleBonusRequest implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2379266080525325489L;

    private Long bonusId;

    private Long articleId;

    private Integer bonusType;

    private Integer bonusSecondType;

    private Integer bonusKind;

    private Integer browseTime;

    private BigDecimal bonusValue;

    private Integer maxPeople;

    // 奖励状态 0 禁用（未生效） 1启用（已生效） 2 暂停中 3 已终止 4 已结束
    private Integer bonusStatus;

    private Long createUser;

    private Date createDate;

    private Date updateDate;

    private Long updateUser;

    List<KgArticleBonusRequest> list;

    private long publishStatus;

    /**
     * @return the publishStatus
     */
    public long getPublishStatus() {
        return publishStatus;
    }

    /**
     * @param publishStatus
     *            the publishStatus to set
     */
    public void setPublishStatus(long publishStatus) {
        this.publishStatus = publishStatus;
    }

    /**
     * @return the list
     */
    public List<KgArticleBonusRequest> getList() {
        return list;
    }

    /**
     * @param list
     *            the list to set
     */
    public void setList(List<KgArticleBonusRequest> list) {
        this.list = list;
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

    public Integer getBonusType() {
        return bonusType;
    }

    public void setBonusType(Integer bonusType) {
        this.bonusType = bonusType;
    }

    public Integer getBonusSecondType() {
        return bonusSecondType;
    }

    public void setBonusSecondType(Integer bonusSecondType) {
        this.bonusSecondType = bonusSecondType;
    }

    public Integer getBonusKind() {
        return bonusKind;
    }

    public void setBonusKind(Integer bonusKind) {
        this.bonusKind = bonusKind;
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
}
