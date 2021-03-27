package com.kg.platform.model.in;

/**
 * Created by Administrator on 2018/3/26.
 */
public class PushMessageInModel {

    private Long message_id;

    private String messageText;

    private Long createDate;

    private Integer readState;//0:未读 1:已读

    private String title;

    private String receive;//接收者  有值：发送给指定人  "all"：发送给所有人

    private Integer type;//消息类型 	1：系统通知 2.动态 3.资讯

    private Integer pushPlace;//推送位置 	0：内部和手机 1：内部 2：手机

    private String messageAvatar;//消息头像

    public Long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Long message_id) {
        this.message_id = message_id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
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
}
