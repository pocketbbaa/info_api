package com.kg.platform.model.out;

import java.util.Date;

public class KeywordsOutModel {
    private Integer keywordId;

    private String keywordDesc;

    private String keywordLink;

    private Integer secondChannel;

    private Boolean keywordStatus;

    private Integer keywordOrder;

    private Integer createUser;

    private Date createDate;

    private Integer updateUser;

    private Date updateDate;

    private long keywordInsite;

    /**
     * @return the keywordInsite
     */
    public long getKeywordInsite() {
        return keywordInsite;
    }

    /**
     * @param keywordInsite
     *            the keywordInsite to set
     */
    public void setKeywordInsite(long keywordInsite) {
        this.keywordInsite = keywordInsite;
    }

    public Integer getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Integer keywordId) {
        this.keywordId = keywordId;
    }

    public String getKeywordDesc() {
        return keywordDesc;
    }

    public void setKeywordDesc(String keywordDesc) {
        this.keywordDesc = keywordDesc;
    }

    public String getKeywordLink() {
        return keywordLink;
    }

    public void setKeywordLink(String keywordLink) {
        this.keywordLink = keywordLink;
    }

    public Integer getSecondChannel() {
        return secondChannel;
    }

    public void setSecondChannel(Integer secondChannel) {
        this.secondChannel = secondChannel;
    }

    public Boolean getKeywordStatus() {
        return keywordStatus;
    }

    public void setKeywordStatus(Boolean keywordStatus) {
        this.keywordStatus = keywordStatus;
    }

    public Integer getKeywordOrder() {
        return keywordOrder;
    }

    public void setKeywordOrder(Integer keywordOrder) {
        this.keywordOrder = keywordOrder;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}