package com.kg.platform.enumeration;

/**
 * Created by Administrator on 2018/7/18.
 */
public enum AndoridChannelEnum {


    defalut_channel(1,"defalut_channel","Android官方渠道"),
    channel_xiaomi(2,"channel_xiaomi","小米应用市场"),
    channel_vivo(3,"channel_vivo","vivo应用市场"),
    channel_sougou(4,"channel_sougou","搜狗应用市场"),
    channel_360(5,"channel_360","360应用市场"),
    channel_baidu(6,"channel_baidu","百度应用市场"),
    channel_lianxiang(7,"channel_lianxiang","联想应用市场"),
    channel_meizu(8,"channel_meizu","魅族应用市场"),
    channel_oppo(9,"channel_oppo","oppo应用市场"),
    channel_other(10,"channel_other","其它应用市场"),
    channel_pp(11,"channel_pp","pp手机应用市场"),
    channel_qudao1(12,"channel_qudao1","渠道1"),
    channel_qudao2(13,"channel_qudao2","渠道2"),
    channel_qudao3(14,"channel_qudao3","渠道3"),
    channel_yingyongbao(15,"channel_yingyongbao","应用宝"),
    channel_huawei(16,"channel_huawei","华为应用市场");


    private Integer channelKey;
    private String channelName;
    private String channelMean;

    AndoridChannelEnum() {
    }

    AndoridChannelEnum(Integer channelKey, String channelName, String channelMean) {
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
