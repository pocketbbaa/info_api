package com.kg.platform.model.request.admin;

import java.util.Date;

public class FeedbackQueryRequest extends AdminBaseRequest {

    /**
     * 
     */
    private static final long serialVersionUID = 5023183871840495288L;

    private String content;

    private Date startDate;

    private Date endDate;

    private Boolean status;

    private String feedbackId;

    private String title;

    private String email;

    private String replayInfo;

    private Integer feedbackType;//反馈意见类型1：功能建议，2：内容建议，3：体验建议 4.投诉

    public Integer getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(Integer feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getReplayInfo() {
        return replayInfo;
    }

    public void setReplayInfo(String replayInfo) {
        this.replayInfo = replayInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
