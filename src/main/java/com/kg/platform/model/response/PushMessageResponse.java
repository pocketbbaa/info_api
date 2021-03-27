package com.kg.platform.model.response;

/**
 * Created by Administrator on 2018/3/26.
 */
public class PushMessageResponse {

    private String message_id;

    private String messageText;

    private String createDate;

    private Integer readState;// 0:未读 1:已读

    private String title;

    private String receive;// 接收者 有值：发送给指定人 "all"：发送给所有人

    private Integer type;// 消息类型 1：系统通知 2.动态 3.资讯

    private Integer pushPlace;// 推送位置 0：内部和手机 1：内部 2：手机

    private String messageAvatar;// 消息头像

    private String createDateTimestamp;

    private Integer classify; // 1:收到奖励 2:评论 3系统通知 4.新增粉丝

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateDateTimestamp() {
        return createDateTimestamp;
    }

    public void setCreateDateTimestamp(String createDateTimestamp) {
        this.createDateTimestamp = createDateTimestamp;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getReadState() {
        return readState;
    }

    public void setReadState(Integer readState) {
        this.readState = readState;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPushPlace() {
        return pushPlace;
    }

    public void setPushPlace(Integer pushPlace) {
        this.pushPlace = pushPlace;
    }

    public String getMessageAvatar() {
        return messageAvatar;
    }

    public void setMessageAvatar(String messageAvatar) {
        this.messageAvatar = messageAvatar;
    }

    public Integer getClassify() {
        return classify;
    }

    public void setClassify(Integer classify) {
        this.classify = classify;
    }

}
