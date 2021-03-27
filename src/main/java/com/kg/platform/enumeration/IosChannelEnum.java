package com.kg.platform.enumeration;

/**
 * Created by Administrator on 2018/7/18.
 */
public enum IosChannelEnum {
    defalut_channel(17,"defalut_channel","IOS官方渠道"),
    channel_majia1(18,"channel_majia1","马甲1"),
    channel_majia2(19,"channel_majia2","马甲2"),
    channel_majia3(20,"channel_majia3","马甲3");


    private Integer channelKey;
    private String channelName;
    private String channelMean;

    IosChannelEnum() {
    }

    IosChannelEnum(Integer channelKey, String channelName, String channelMean) {
        this.channelKey = channelKey;
        this.channelName = channelName;
        this.channelMean = channelMean;
    }

    public Integer getChannelKey() {
        return channelKey;
    }

    public void setChannelKey(Integer channelKey) {
        this.channelKey = channelKey;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelMean() {
        return channelMean;
    }

    public void setChannelMean(String channelMean) {
        this.channelMean = channelMean;
    }
}
