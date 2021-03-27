package com.kg.platform.dao.entity;

import java.util.Date;

public class Feedback {
    private Long feedbackId;

    private String feedbackDetail;

    private String feedbackEmail;

    private String feedbackPhone;

    private String fromUrl;

    private Boolean feedbackStatus;

    private String replayInfo;

    private Date createDate;

    private Long createUser;

    private Date updateDate;

    private Integer updateUser;

    private Integer feedbackType;

    private Integer fromType;

    public Integer getFromType() {
        return fromType;
    }

    public void setFromType(Integer fromType) {
        this.fromType = fromType;
    }

    public Integer getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(Integer feedbackType) {
        this.feedbackType = feedbackType;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getFeedbackDetail() {
        return feedbackDetail;
    }

    public void setFeedbackDetail(String feedbackDetail) {
        this.feedbackDetail = feedbackDetail;
    }

    public String getFeedbackEmail() {
        return feedbackEmail;
    }

    public void setFeedbackEmail(String feedbackEmail) {
        this.feedbackEmail = feedbackEmail;
    }

    public String getFeedbackPhone() {
        return feedbackPhone;
    }

    public void setFeedbackPhone(String feedbackPhone) {
        this.feedbackPhone = feedbackPhone;
    }

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }

    public Boolean getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setFeedbackStatus(Boolean feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    public String getReplayInfo() {
        return replayInfo;
    }

    public void setReplayInfo(String replayInfo) {
        this.replayInfo = replayInfo;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }
}