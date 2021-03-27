package com.kg.platform.model.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class KgArticleBonusResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7689198078656945500L;

    List<KgArticleBonusResponse> list;

    private String bonusId;

    private String articleId;

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

    private String updateUser;

    private String bonusTypeid;

    private String bonusTypename;

    private String balance;

    // 奖励已领取金额
    private String bonusGetValue;

    // 奖励已领取人数
    private Integer bonusGetNum;

    // 奖励总金额
    private String bonusTotalValue;

    public String getBonusGetValue() {
        return bonusGetValue;
    }

    public void setBonusGetValue(String bonusGetValue) {
        this.bonusGetValue = bonusGetValue;
    }

    public String getBonusTotalValue() {
        return bonusTotalValue;
    }

    public void setBonusTotalValue(String bonusTotalValue) {
        this.bonusTotalValue = bonusTotalValue;
    }

    public Integer getBonusGetNum() {
        return bonusGetNum;
    }

    public void setBonusGetNum(Integer bonusGetNum) {
        this.bonusGetNum = bonusGetNum;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

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

    public String getBonusId() {
        return bonusId;
    }

    public void setBonusId(String bonusId) {
        this.bonusId = bonusId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Integer getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(Integer bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return the list
     */
    public List<KgArticleBonusResponse> getList() {
        return list;
    }

    /**
     * @param list
     *            the list to set
     */
    public void setList(List<KgArticleBonusResponse> list) {
        this.list = list;
    }

}
