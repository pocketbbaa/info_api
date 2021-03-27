package com.kg.platform.model.response.admin;

import java.util.Date;

public class FeedbackQueryResponse extends AdminBaseResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -6632584496528976940L;

    private String id;

    private String content;

    private String email;

    private String phone;

    private Date createDate;

    private String statusDisplay;

    private Boolean status;

    private String fromUrl;

    private String replayInfo;

    private Integer feedbackType; //反馈意见类型1：功能建议，2：内容建议，3：体验建议 4.投诉

    private Integer fromType;  //来源 1：web ，2：app

    public Integer getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(Integer feedbackType) {
        this.feedbackType = feedbackType;
    }

    public Integer getFromType() {
        return fromType;
    }

    public void setFromType(Integer fromType) {
        this.fromType = fromType;
    }

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }

    public String getReplayInfo() {
        return replayInfo;
    }

    public void setReplayInfo(String replayInfo) {
        this.replayInfo = replayInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
