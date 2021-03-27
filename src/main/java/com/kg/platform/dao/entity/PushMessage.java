package com.kg.platform.dao.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/26.
 */
public class PushMessage {

    private Long _id;

    private String title;

    private String messageText;

    private Long userId;// 评论/点赞/分享 userId 后面根据场景应用

    private Long createDate;

    private Integer readState;// 0:未读 1:已读

    private List<String> receive;// 接收者 有值：发送给指定人

    /**
     * 推送全部用户 1
     * 推送指定用户 2
     * 绑定别名 3
     * 解绑别名 4
     * 绑定tag  5
     * 解绑tag  6
     */
    private Integer type;// 消息类型 1：系统通知 2.动态 3.资讯 4.绑定 5.解绑 6.tag绑定 7.tag解绑

    private Integer pushPlace;// 推送位置 0：内部和手机 1：内部 2：手机

    private String messageAvatar;// 消息头像

    private String deviceId;// APP设备ID

    private Integer osVersion;// 1:IOS 2:ANDROID

    private String remark;// 扩充字段（jsonMap）

    private String tag;

    private Integer classify;// 1.奖励 2.动态 3系统通知

    private Integer secondClassify;// 1:收到奖励 2:评论 3系统通知 4.新增粉丝

    private String commentUserId;// 评论人ID

    public static String mongoTable = "pushMessage";

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(Integer osVersion) {
        this.osVersion = osVersion;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
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

    public List<String> getReceive() {
        return receive;
    }

    public void setReceive(List<String> receive) {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getClassify() {
        return classify;
    }

    public void setClassify(Integer classify) {
        this.classify = classify;
    }

    public Integer getSecondClassify() {
        return secondClassify;
    }

    public void setSecondClassify(Integer secondClassify) {
        this.secondClassify = secondClassify;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

}
