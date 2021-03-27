package com.kg.platform.common.entity;

/**
 * Created by Administrator on 2018/7/20.
 */
public class IosNotice{
    private String aliMessageId;//阿里云消息ID
    private String serviceId;//业务ID
    private String noticeType;//通知类型
    private String clickDate;//点击时间

    public String getAliMessageId() {
        return aliMessageId;
    }

    public void setAliMessageId(String aliMessageId) {
        this.aliMessageId = aliMessageId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getClickDate() {
        return clickDate;
    }

    public void setClickDate(String clickDate) {
        this.clickDate = clickDate;
    }
}
