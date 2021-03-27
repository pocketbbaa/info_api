package com.kg.platform.model.in;

import com.alibaba.fastjson.JSON;
import com.kg.platform.enumeration.PushClassifyEnum;
import com.kg.platform.enumeration.PushTypeEnum;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 推送结构
 */
public class MessageInModel {

    private String title;

    private String message;

    private String recieve;

    private String tags;

    /**
     * 检查登录
     * 0不需要 1需要
     */
    private int checkLogin;

    /**
     * 系统消息 0
     * 奖励消息 1
     * 动态消息 2
     * 快讯更新 3
     * 热点资讯 4
     */
    private int classify;

    /**
     * 推送全部用户 1
     * 推送指定用户 2
     * 绑定别名 3
     * 解绑别名 4
     * 绑定tag  5
     * 解绑tag  6
     */
    private Integer type;

    /**
     * 推送位置
     * 0：内部消息列表+手机通知
     * 1：内部消息列表
     * 2：手机通知
     */
    private Integer pushPlace;// 推送位置 0：内部和手机 1：内部 2：手机

    private String deviceId;// APP设备ID

    private Integer osVersion;// 1:IOS 2:ANDROID

    private String messageAvatar;// 消息头像

    private String remark;// 扩充字段（jsonMap）

    /**
     * 构建消息
     *
     * @param recieve
     * @param title
     * @param message
     * @param extra
     * @param pushClassifyEnum
     * @param pushTypeEnum
     * @param tag
     * @param checkLogin
     * @return
     */
    public static MessageInModel buildMessage(String recieve, String title, String message, Map<String, Object> extra, PushClassifyEnum pushClassifyEnum, PushTypeEnum pushTypeEnum, String tag, int checkLogin, Integer pushPlace) {
        MessageInModel messageInModel = new MessageInModel();
        messageInModel.setMessage(message);
        messageInModel.setTitle(title);
        messageInModel.setRecieve(recieve);
        messageInModel.setClassify(pushClassifyEnum.getCode());
        messageInModel.setType(pushTypeEnum.getCode());
        messageInModel.setTags(tag);
        messageInModel.setCheckLogin(checkLogin);
        messageInModel.setPushPlace(pushPlace);
        if (!CollectionUtils.isEmpty(extra)) {
            messageInModel.setRemark(JSON.toJSONString(extra));
        }
        return messageInModel;
    }

    /**
     * 构建消息头像
     *
     * @param messageAvatar
     * @return
     */
    public MessageInModel buildMessageAvatar(String messageAvatar) {
        if (!StringUtils.isEmpty(messageAvatar)) {
            this.messageAvatar = messageAvatar;
        }
        return this;
    }

    /**
     * 构建绑定别名消息
     *
     * @param deviceId
     * @param osVersion
     * @param recieve
     * @return
     */
    public static MessageInModel buildPushBindInfoMessage(String deviceId, Integer osVersion, String recieve, String tag, PushTypeEnum pushTypeEnum, Integer pushPlace) {
        MessageInModel messageInModel = new MessageInModel();
        messageInModel.setRecieve(recieve);
        messageInModel.setDeviceId(deviceId);
        messageInModel.setOsVersion(osVersion);
        messageInModel.setTags(tag);
        messageInModel.setType(pushTypeEnum.getCode());
        messageInModel.setPushPlace(pushPlace);
        return messageInModel;
    }

    public String getMessageAvatar() {
        return messageAvatar;
    }

    public void setMessageAvatar(String messageAvatar) {
        this.messageAvatar = messageAvatar;
    }

    public Integer getPushPlace() {
        return pushPlace;
    }

    public void setPushPlace(Integer pushPlace) {
        this.pushPlace = pushPlace;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecieve() {
        return recieve;
    }

    public void setRecieve(String recieve) {
        this.recieve = recieve;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getCheckLogin() {
        return checkLogin;
    }

    public void setCheckLogin(int checkLogin) {
        this.checkLogin = checkLogin;
    }

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}