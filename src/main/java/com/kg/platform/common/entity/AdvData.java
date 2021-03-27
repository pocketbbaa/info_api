package com.kg.platform.common.entity;

/**
 * Created by Administrator on 2018/7/20.
 */
public class AdvData{
    private String advId;//广告ID
    private String exposure;//曝光量
    private String clickNum;//点击量

    public String getAdvId() {
        return advId;
    }

    public void setAdvId(String advId) {
        this.advId = advId;
    }

    public String getExposure() {
        return exposure;
    }

    public void setExposure(String exposure) {
        this.exposure = exposure;
    }

    public String getClickNum() {
        return clickNum;
    }

    public void setClickNum(String clickNum) {
        this.clickNum = clickNum;
    }
}
