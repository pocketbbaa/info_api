package com.kg.platform.dao.entity;

import java.io.Serializable;
import java.util.Date;

public class SensitiveWords implements Serializable {

    private static final long serialVersionUID = -91295258424293824L;

    private Integer wordId;

    private String wordDesc;

    private Integer wordType;

    private Integer createUser;

    private Date createDate;

    private Integer updateUser;

    private Date updateDate;

    public Integer getWordId() {
        return wordId;
    }

    public void setWordId(Integer wordId) {
        this.wordId = wordId;
    }

    public String getWordDesc() {
        return wordDesc;
    }

    public void setWordDesc(String wordDesc) {
        this.wordDesc = wordDesc;
    }

    public Integer getWordType() {
        return wordType;
    }

    public void setWordType(Integer wordType) {
        this.wordType = wordType;
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